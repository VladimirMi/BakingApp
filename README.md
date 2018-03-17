# BakingApp
The third project for "Android developer nanodegree" (google developer scholarship)

### Note
On some devices, when playing videos from backend, player crashes when attempting to resume playback after resuming activity
(i.e orientation change, to home screen and back). But on different video samples this behavior is not observed.
Apparently, Exoplayer on some devices can not handle video with such inadequate bitrate (more than 12Mbps).
Harcoded sample urls for test purposes may be found in .data.PlayerHolder#prepare(String videoUrl).


## License
```
MIT License

Copyright (c) 2018 Vladimir Mikhalev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
