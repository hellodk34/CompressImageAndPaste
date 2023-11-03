# CompressImageAndPaste

压缩剪贴板图片并立即写入剪贴板，接着在任意地方粘贴压缩后的图片。

## 更新日志

- v1.0.1 发现 jdk 内置的压缩效果实在太差，最终引入了 net.coobird 的 thumbnailator 库进行图片的压缩
- v1.0.0 最初版本发布

---

## 使用指南

使用前请在 release 页面下载 `compress_image_and_paste.jar`，确保本地有安装 JDK11 环境并且正确配置环境变量（能在任意路径下都能正确执行 `java --version`）

在合适的路径下运行 `java -jar compress_image_and_paste.jar` 即可。

```
# java -jar compress_image_and_paste.jar
Make sure your clipboard latest item is image and continue.
Jar usage description: read your clipboard image and compress it, finally write the compressed image to your clipboard, you can paste it anywhere.

# java -jar compress_image_and_paste.jar
Original image size is 9.18 MB.
Compressed image size is 1.47 MB, and has been written to your clipboard, you can paste it anywhere.
```

欢迎下载使用。

更详细的指南请阅读文章 [压缩剪贴板内图片并立即写入剪贴板，减轻服务器存储压力](https://hellodk.cn/post/1150) .
