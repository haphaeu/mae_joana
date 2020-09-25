# -*- coding: utf-8 -*-
"""
Icon generator

Generates a Gumbel pdf curve in various resolutions.

Created on Fri Sep 15 11:36:23 2017

@author: rarossi
"""

from matplotlib import pyplot as plt
from scipy import stats as ss
import numpy as np

#%%

mon_dpi = 96
icon_sz = (16, 24, 32, 48, 96, 128, 256)

for sz in icon_sz:
    fig_sz = sz/mon_dpi
    lw = sz/10
    x = np.linspace(-2.5, 4, 100)
    y = ss.gumbel_r.pdf(x)
    plt.figure(figsize=(fig_sz, fig_sz), dpi=mon_dpi)
    plt.plot(x, y, color='g', lw=lw)
    plt.xticks(())
    plt.yticks(())
    plt.box(False)
    plt.savefig('icon%d.png' % sz, bbox_inches=0, pad_inches=0)
