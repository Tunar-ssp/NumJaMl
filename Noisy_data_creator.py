import pandas as pd
import numpy as np
import cv2
import csv
from tqdm import tqdm

def sparse_noise(img, density=0.03):
    out = img.copy().astype(np.int16)
    h, w = out.shape
    n = int(h * w * density)

    ys = np.random.randint(0, h, n)
    xs = np.random.randint(0, w, n)
    out[ys, xs] = np.random.randint(160, 256, n)

    n2 = n // 3
    ys2 = np.random.randint(0, h, n2)
    xs2 = np.random.randint(0, w, n2)
    out[ys2, xs2] = 0

    return np.clip(out, 0, 255).astype(np.uint8)


def tiny_pixel_clusters(img, n_clusters=3):
    out = img.copy()
    h, w = out.shape
    for _ in range(n_clusters):
        cx = np.random.randint(0, w)
        cy = np.random.randint(0, h)
        size = np.random.randint(1, 3)
        val = int(np.random.choice([
            np.random.randint(180, 256),
            0,
        ]))
        y1 = max(0, cy)
        y2 = min(h, cy + size)
        x1 = max(0, cx)
        x2 = min(w, cx + size)
        out[y1:y2, x1:x2] = val
    return out


def gaussian_noise(img, sigma=10):
    noise = np.random.normal(0, sigma, img.shape).astype(np.float32)
    fg_mask = img.astype(np.float32) > 20
    result = img.astype(np.float32) + noise * fg_mask
    return np.clip(result, 0, 255).astype(np.uint8)


def screen_clip_shift(img, visibility=0.70):
    h, w = img.shape
    max_off = int(w * (1.0 - visibility))

    direction = np.random.choice(['left', 'right', 'up', 'down'])
    off = np.random.randint(int(max_off * 0.7), max_off + 1)

    if direction == 'left':    tx, ty = -off, np.random.randint(-2, 3)
    elif direction == 'right': tx, ty =  off, np.random.randint(-2, 3)
    elif direction == 'up':    tx, ty = np.random.randint(-2, 3), -off
    else:                      tx, ty = np.random.randint(-2, 3),  off

    M = np.float32([[1, 0, tx], [0, 1, ty]])
    return cv2.warpAffine(img, M, (w, h),
                          borderMode=cv2.BORDER_CONSTANT, borderValue=0)


def elastic_distort(img, alpha=7.0, sigma=3.0):
    h, w = img.shape
    dx = cv2.GaussianBlur(np.random.randn(h, w).astype(np.float32), (0, 0), sigma) * alpha
    dy = cv2.GaussianBlur(np.random.randn(h, w).astype(np.float32), (0, 0), sigma) * alpha
    x, y = np.meshgrid(np.arange(w), np.arange(h))
    mx = np.clip(x + dx, 0, w - 1).astype(np.float32)
    my = np.clip(y + dy, 0, h - 1).astype(np.float32)
    return cv2.remap(img, mx, my, cv2.INTER_LINEAR, borderValue=0)


def augment_image(img, label):
    work = img.copy()

    if label in (6, 9):
        angle = np.random.uniform(-15, 15)
    else:
        angle = np.random.uniform(-28, 28)

    scale = np.random.uniform(0.88, 1.13)
    M_rot = cv2.getRotationMatrix2D((14, 14), angle, scale)
    work = cv2.warpAffine(work, M_rot, (28, 28),
                          borderMode=cv2.BORDER_CONSTANT, borderValue=0)

    if np.random.random() < 0.50:
        work = elastic_distort(
            work,
            alpha=np.random.uniform(3, 8),
            sigma=np.random.uniform(2, 3.5)
        )

    if np.random.random() < 0.22:
        work = screen_clip_shift(work, visibility=np.random.uniform(0.60, 0.80))
    else:
        tx = np.random.randint(-5, 6)
        ty = np.random.randint(-5, 6)
        M2 = np.float32([[1, 0, tx], [0, 1, ty]])
        work = cv2.warpAffine(work, M2, (28, 28),
                              borderMode=cv2.BORDER_CONSTANT, borderValue=0)

    r = np.random.random()
    if r < 0.38:
        work = sparse_noise(work, density=np.random.uniform(0.015, 0.04))
    elif r < 0.65:
        work = tiny_pixel_clusters(work, n_clusters=np.random.randint(2, 7))
    elif r < 0.80:
        work = gaussian_noise(work, sigma=np.random.uniform(8, 18))
    elif r < 0.90:
        work = sparse_noise(work, density=0.02)
        work = tiny_pixel_clusters(work, n_clusters=np.random.randint(1, 4))

    if np.random.random() < 0.15:
        for _ in range(np.random.randint(1, 3)):
            bw = np.random.randint(1, 4)
            bh = np.random.randint(1, 4)
            bx = np.random.randint(0, 29 - bw)
            by = np.random.randint(0, 29 - bh)
            work[by:by+bh, bx:bx+bw] = np.random.choice([0, 255])

    return work


def augment_dataset(input_csv, output_csv, multiplier=5):
    reader = pd.read_csv(input_csv, header=None, chunksize=1000)
    all_rows = []

    for chunk in tqdm(reader, desc="Augmenting"):
        for _, row in chunk.iterrows():
            try:
                label  = int(row.iloc[0])
                pixels = row.iloc[1:].values.astype(np.float32)
            except (ValueError, IndexError):
                continue

            img = pixels.reshape(28, 28).astype(np.uint8)
            all_rows.append([label] + pixels.astype(int).tolist())

            for _ in range(multiplier - 1):
                aug = augment_image(img, label)
                all_rows.append([label] + aug.flatten().tolist())

    np.random.shuffle(all_rows)

    with open(output_csv, 'w', newline='') as f:
        csv.writer(f).writerows(all_rows)

if __name__ == "__main__":
    augment_dataset(
        input_csv  = 'data/mnist_train.csv',
        output_csv = 'data/mnist_extreme_v6.csv',
        multiplier = 8
    )