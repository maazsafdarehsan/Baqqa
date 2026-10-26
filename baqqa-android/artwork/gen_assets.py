import os, glob
from PIL import Image, ImageDraw

RES = r'E:/Baqqa/briar/baqqa-android/src/main/res'
LOGO = r'E:/Baqqa/briar/baqqa-android/src/main/assets/baqqa_logo.png'

im = Image.open(LOGO).convert('RGBA')
W, H = im.size
print('logo:', W, H, im.getpixel((0,0)), im.getpixel((W-1,H-1)))

# Determine background: opaque dark -> white-on-black; else assume white-on-transparent alpha
corner = im.getpixel((0, 0))
opaque_dark = corner[3] > 200 and sum(corner[:3]) / 3 < 60

# Build a white-mark alpha mask
if opaque_dark:
    gray = im.convert('L')
    mark = Image.new('RGBA', (W, H), (0, 0, 0, 0))
    white = Image.new('RGBA', (W, H), (255, 255, 255, 255))
    mark = Image.composite(white, mark, gray.point(lambda p: 255 if p > 100 else 0))
else:
    gray = im.split()[3]
    mark = Image.new('RGBA', (W, H), (255, 255, 255, 0))
    white = Image.new('RGBA', (W, H), (255, 255, 255, 255))
    mark = Image.composite(white, mark, gray.point(lambda p: 255 if p > 20 else 0))

# Tight bbox of visible mark+wordmark
bbox = mark.getbbox()
print('bbox:', bbox)
mark = mark.crop(bbox)
W2, H2 = mark.size
print('cropped logo:', W2, H2)

# Extract square mark = leftmost square region of the logo (aspect ratio W2/H2 > 2 means wordmark present)
if W2 > H2 * 1.6:
    sq = mark.crop((0, 0, H2, H2))
else:
    sq = mark
print('mark size:', sq.size)

def paste_center(canvas, art, scale):
    cw, ch = canvas.size
    target = int(min(cw, ch) * scale)
    ratio = art.width / art.height
    tw = target if ratio >= 1 else int(target * ratio)
    th = int(target / ratio) if ratio >= 1 else target
    a = art.resize((tw, th), Image.LANCZOS)
    canvas.alpha_composite(a, ((cw - tw) // 2, (ch - th) // 2))

BLACK = (0, 0, 0, 255)

# --- Adaptive foreground (108dp -> xxxhdpi 324px source; vector-less PNG per density) ---
fg_sizes = {'mdpi': 108, 'hdpi': 162, 'xhdpi': 216, 'xxhdpi': 324, 'xxxhdpi': 432}
for d, s in fg_sizes.items():
    c = Image.new('RGBA', (s, s), (0, 0, 0, 0))
    paste_center(c, sq, 0.42)  # mark centered in safe zone (66/108)
    c.save(os.path.join(RES, f'mipmap-{d}', 'ic_launcher_foreground.png'))

# --- Legacy launcher icons: black bg + white mark (square + circle-masked round) ---
leg = {'mdpi': 48, 'hdpi': 72, 'xhdpi': 96, 'xxhdpi': 144, 'xxxhdpi': 192}
for d, s in leg.items():
    sqi = Image.new('RGBA', (s, s), BLACK)
    paste_center(sqi, sq, 0.62)
    sqi.save(os.path.join(RES, f'mipmap-{d}', 'ic_launcher.png'))
    rnd = Image.new('RGBA', (s, s), (0, 0, 0, 0))
    paste_center(rnd, sqi, 1.0)
    m = Image.new('L', (s, s), 0)
    ImageDraw.Draw(m).ellipse((0, 0, s - 1, s - 1), fill=255)
    rnd.putalpha(m)
    rnd.save(os.path.join(RES, f'mipmap-{d}', 'ic_launcher_round.png'))

# --- Splash: 1024x1024 black, mark on top + wordmark below (mark from left crop, wordmark from right) ---
splash = Image.new('RGBA', (1024, 1024), BLACK)
paste_center(splash, sq, 0.40)
# wordmark: right portion of cropped logo (logo without the left square)
if W2 > H2 * 1.6:
    wm = mark.crop((H2, 0, W2, H2))
    wm_canvas = Image.new('RGBA', (1024, 1024), (0, 0, 0, 0))
    tw = 560
    th = int(wm.height * tw / wm.width)
    wmr = wm.resize((tw, th), Image.LANCZOS)
    # position under the centered mark
    mk_h = int(1024 * 0.40 * sq.height / sq.width) if sq.width >= sq.height else int(1024 * 0.40)
    y = (1024 - th) // 2 + mk_h // 2 + 60
    wm_canvas.alpha_composite(wmr, ((1024 - tw) // 2, min(y, 1024 - th)))
    splash.alpha_composite(wm_canvas)
splash.convert('RGB').save(os.path.join(RES, 'drawable', 'splash_screen.png'))

print('OK - all assets generated')
for f in sorted(glob.glob(os.path.join(RES, 'mipmap-*', '*.png'))) + [os.path.join(RES, 'drawable', 'splash_screen.png')]:
    print(f, os.path.getsize(f))
