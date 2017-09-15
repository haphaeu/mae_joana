from numpy import sqrt, linspace
from time import time

t0 = time()
print('Starting...')
sqrt(linspace(0,100000000-1,100000000))
print('Done')
tf = time() - t0
print(tf)