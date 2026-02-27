import os
import pandas as pd
import numpy as np
import tkinter as tk
from tkinter import ttk, messagebox
from matplotlib.figure import Figure
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

DATA_FILES = {
    "Train Set": "data/mnist_extreme_v6.csv",
    "Test Set": "mnist_test.csv"
}

class NumJaStudio:
    def __init__(self, root):
        self.root = root
        self.root.title("NumJa")
        self.root.geometry("1500x950")
        self.root.configure(bg="#0a0a0a")

        self.full_df = None
        self.display_df = None
        self.page_offset = 0
        self.page_size = 24

        self.setup_ui()
        self.load_dataset(DATA_FILES["Train Set"])

    def setup_ui(self):
        self.sidebar = tk.Frame(self.root, bg="#111111", width=250)
        self.sidebar.pack(side=tk.LEFT, fill=tk.Y)
        self.sidebar.pack_propagate(False)

        tk.Label(self.sidebar, text="NumJa", fg="#00e5ff", bg="#111111", font=("Impact", 28)).pack(pady=(20, 0))
        tk.Label(self.sidebar, text="DATA EXPLORER", fg="#555", bg="#111111", font=("Arial", 10, "bold")).pack(pady=(0, 20))


        self.add_section_label("DATA SOURCE")
        self.set_selector = ttk.Combobox(self.sidebar, values=list(DATA_FILES.keys()), state="readonly")
        self.set_selector.current(0)
        self.set_selector.pack(padx=15, pady=5, fill=tk.X)
        self.set_selector.bind("<<ComboboxSelected>>", lambda e: self.load_dataset(DATA_FILES[self.set_selector.get()]))

        self.add_section_label("FILTER BY DIGIT")
        self.digit_filter = ttk.Combobox(self.sidebar, values=["All"] + [str(i) for i in range(10)], state="readonly")
        self.digit_filter.current(0)
        self.digit_filter.pack(padx=15, pady=5, fill=tk.X)
        self.digit_filter.bind("<<ComboboxSelected>>", self.apply_filter)

        self.add_section_label("JUMP TO INDEX")
        self.idx_entry = tk.Entry(self.sidebar, bg="#222", fg="#00ff41", insertbackground="white", borderwidth=0)
        self.idx_entry.pack(padx=15, pady=5, fill=tk.X)
        tk.Button(self.sidebar, text="GO", command=self.jump_to_index, bg="#333", fg="white").pack(padx=15, fill=tk.X)


        self.add_section_label("RANDOM SAMPLE")
        self.rand_entry = tk.Entry(self.sidebar, bg="#222", fg="#00ff41", borderwidth=0)
        self.rand_entry.insert(0, "24")
        self.rand_entry.pack(padx=15, pady=5, fill=tk.X)
        tk.Button(self.sidebar, text="SHUFFLE BATCH", command=self.get_random_sample, bg="#005f6b", fg="white").pack(padx=15, fill=tk.X)

        self.add_section_label("CONTROLS")
        tk.Button(self.sidebar, text="PREV PAGE", command=self.prev_page, bg="#222", fg="white").pack(side=tk.LEFT, padx=(15, 5), pady=10, expand=True, fill=tk.X)
        tk.Button(self.sidebar, text="NEXT PAGE", command=self.next_page, bg="#222", fg="white").pack(side=tk.LEFT, padx=(5, 15), pady=10, expand=True, fill=tk.X)

        self.stats_label = tk.Label(self.sidebar, text="", fg="#00ff41", bg="#111111", font=("Consolas", 9), justify=tk.LEFT)
        self.stats_label.pack(side=tk.BOTTOM, pady=20, padx=15, fill=tk.X)

 
        self.gallery_frame = tk.Frame(self.root, bg="#0a0a0a")
        self.gallery_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True, padx=10, pady=10)

        self.fig = Figure(figsize=(10, 8), dpi=100, facecolor="#0a0a0a")
        self.canvas = FigureCanvasTkAgg(self.fig, master=self.gallery_frame)
        self.canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)

    def add_section_label(self, text):
        tk.Label(self.sidebar, text=text, fg="#888", bg="#111111", font=("Arial", 8, "bold")).pack(pady=(20, 0), padx=15, anchor="w")

    def load_dataset(self, filepath):
        try:
            if not os.path.exists(filepath):
                messagebox.showerror("Error", f"File {filepath} not found!")
                return

            self.full_df = pd.read_csv(filepath, header=None)
            self.display_df = self.full_df
            self.page_offset = 0
            self.update_display()
        except Exception as e:
            messagebox.showerror("Load Error", str(e))

    def apply_filter(self, event=None):
        selection = self.digit_filter.get()
        if selection == "All":
            self.display_df = self.full_df
        else:
            digit = int(selection)
            self.display_df = self.full_df[self.full_df[0] == digit]
        
        self.page_offset = 0
        self.update_display()

    def jump_to_index(self):
        try:
            idx = int(self.idx_entry.get())
            if 0 <= idx < len(self.display_df):
                self.page_offset = (idx // self.page_size) * self.page_size
                self.update_display()
            else:
                messagebox.showwarning("Index Error", "Index out of range for current filter.")
        except ValueError:
            pass

    def get_random_sample(self):
        try:
            num = int(self.rand_entry.get())
            num = max(1, min(num, 100))
            self.page_size = num
            self.display_df = self.full_df.sample(n=num)
            self.page_offset = 0
            self.update_display()
        except ValueError:
            pass

    def update_display(self):
        if self.display_df is None or self.display_df.empty:
            return

        data_slice = self.display_df.iloc[self.page_offset : self.page_offset + self.page_size].values
        
        self.fig.clear()
        n = len(data_slice)
        cols = 6 if n >= 6 else n
        rows = int(np.ceil(n / cols)) if n > 0 else 1
        
        self.fig.suptitle(f"VIEWING {n} SAMPLES | START INDEX: {self.page_offset}", color="white", fontsize=10)

        for i in range(n):
            label = int(data_slice[i, 0])
            pixels = data_slice[i, 1:].reshape(28, 28)
            
            ax = self.fig.add_subplot(rows, cols, i + 1)
            ax.imshow(pixels, cmap='magma')
            ax.set_title(f"L: {label}", color="#00e5ff", fontsize=8, pad=2)
            ax.axis('off')
        
        self.fig.tight_layout(pad=1.5)
        self.canvas.draw()
        
        # Update Stats
        self.stats_label.config(text=f"DATASET TOTAL: {len(self.full_df)}\nFILTERED TOTAL: {len(self.display_df)}\nRANGE: {self.page_offset} - {self.page_offset + n}")

    def next_page(self):
        if self.page_offset + self.page_size < len(self.display_df):
            self.page_offset += self.page_size
            self.update_display()

    def prev_page(self):
        if self.page_offset >= self.page_size:
            self.page_offset -= self.page_size
            self.update_display()

if __name__ == "__main__":
    root = tk.Tk()
    app = NumJaStudio(root)
    root.mainloop()