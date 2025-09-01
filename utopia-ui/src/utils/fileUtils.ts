/**
 * 从视频文件中提取指定时间点的一帧，并返回图片 Blob
 * @param videoFile - 视频文件 (File)
 * @param timeInSeconds - 要提取的帧的时间点（单位：秒）
 * @returns Promise<Blob> 图片 Blob（PNG 格式）
 */
export const extractFrameFromVideo = (
  videoFile: File,
  timeInSeconds: number = 3
): Promise<File> => {
  // 创建临时 URL 指向视频文件
  const videoObjectUrl = URL.createObjectURL(videoFile)
  const video = document.createElement('video')
  video.src = videoObjectUrl
  video.currentTime = timeInSeconds
  // 设置视频为静音并加载数据
  video.muted = true
  video.autoplay = true
  video.playsInline = true
  // 返回一个 Promise，等待视频 seek 完成
  const canvas = document.createElement('canvas')
  const ctx = canvas.getContext('2d')
  return new Promise<File>((resolve, reject) => {
    // 监听视频元数据加载完成
    video.addEventListener('loadeddata', () => {
      try {
        // 设置 canvas 尺寸为视频尺寸
        canvas.width = video.videoWidth
        canvas.height = video.videoHeight
        // 绘制当前帧到 canvas
        ctx?.drawImage(video, 0, 0, canvas.width, canvas.height)
        // 将 canvas 转为 Blob
        canvas.toBlob((blob) => {
          if (blob) {
            resolve(new File([blob], new Date().getTime() + '.webp', { type: 'image/webp' }))
          } else {
            reject(new Error('Canvas 转 Blob 失败！'))
          }
          // 清理内存
          URL.revokeObjectURL(videoObjectUrl)
        }, 'image/webp')
      } catch (err) {
        reject(err)
      }
    })
    video.addEventListener('error', () => {
      reject(new Error('视频加载失败，请检查文件格式！'))
      URL.revokeObjectURL(videoObjectUrl)
    })
    // 跳转到指定时间点 必须先 load 才能 seek
    video.load()
  })
}
