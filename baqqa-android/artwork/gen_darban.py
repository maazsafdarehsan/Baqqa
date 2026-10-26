# Generates transparent Darban derivatives from the approved Assets artwork.
# Source: E:/Baqqa/Assets/*.jpg (white-background). Keying: near-white -> alpha.
from PIL import Image
import os

SRC = r"E:/Baqqa/Assets"
OUT = r"E:/Baqqa/briar/baqqa-android/src/main/res/drawable-nodpi"
POSES = ["Waving.jpg", "Shield.jpg", "Pointing.jpg"]
HEIGHT = 360  # display height; screens scale down


def key_white(im, lo=225, hi=255):
    rgba = im.convert("RGBA")
    px = rgba.load()
    w, h = rgba.size
    for y in range(h):
        for x in range(w):
            r, g, b, a = px[x, y]
            lum = (r + g + b) / 3.0
            if lum >= hi:
                px[x, y] = (r, g, b, 0)
            elif lum >= lo:
                # smooth falloff to hide jpeg fringing
                alpha = int(255 * (hi - lum) / (hi - lo))
                px[x, y] = (r, g, b, alpha)
    return rgba


os.makedirs(OUT, exist_ok=True)
for name in POSES:
    im = Image.open(os.path.join(SRC, name))
    im = key_white(im)
    bbox = im.getbbox()
    if bbox:
        im = im.crop(bbox)
    ratio = HEIGHT / im.height
    im = im.resize((max(1, int(im.width * ratio)), HEIGHT), Image.LANCZOS)
    out = os.path.join(OUT, "darban_" + name.split(".")[0].lower() + ".png")
    im.save(out, "PNG", optimize=True)
    print(out, im.size, os.path.getsize(out) // 1024, "KB")
