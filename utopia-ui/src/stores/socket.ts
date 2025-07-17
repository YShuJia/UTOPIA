import { defineStore } from 'pinia'
import type { CommentDTO, CommentVO } from '@/request/api/comment'
import { io, Socket } from 'socket.io-client'
import { getToken } from '@/utils/tokenUtils'

export const useSocketStore = defineStore('socket', () => {
  // 评论 socket
  let commentSocket: Socket | null = null
  const initCommentSocket = (onCommentReceived: (comment: CommentVO) => void) => {
    // manager.open()
    const token = getToken()
    if (!commentSocket && token) {
      commentSocket = io(import.meta.env.VITE_COMMENT_SOCKET, {
        path: import.meta.env.VITE_SOCKET_PATH,
        transports: ['websocket'],
        // 允许跨域请求
        withCredentials: true,
        reconnection: true,
        reconnectionAttempts: 3,
        reconnectionDelayMax: 10000
      })

      commentSocket.on('connect', () => {
        console.log('Connected to Socket.IO server')
      })

      commentSocket.on('disconnect', () => {
        console.log('Disconnected from Socket.IO server')
      })

      commentSocket.on('commentMessage', (data: CommentVO) => {
        onCommentReceived(data)
      })
    }
  }
  // 发送消息
  const sendCommentSocket = (message: CommentDTO) => {
    if (commentSocket) {
      commentSocket.emit('commentSocket', message)
    }
  }

  const connectCommentSocket = () => {
    if (commentSocket) {
      commentSocket.connect()
    }
  }

  return {
    initCommentSocket,
    sendCommentSocket,
    connectCommentSocket
  }
})
