// @ts-ignore
import { sm2, sm4 } from 'sm-crypto'

export class SmUtils {
  public static keypair = sm2.generateKeyPairHex()
  // 1 - C1C3C2，0 - C1C2C3，默认为1
  private static cipherMode: number = 0

  /**
   * 使用 SM2 加密
   * @param publicKey 公钥加密
   * @param msg - 消息
   */
  static enSm2 = (publicKey: string, msg: any): string => {
    return '04' + sm2.doEncrypt(msg.toString(), publicKey, this.cipherMode)
  }

  /**
   * 使用 SM2 解密
   * @param privateKey
   * @param msg - 加密后的消息
   */

  static deSm2 = (privateKey: string, msg: string): string => {
    try {
      if (msg.startsWith('04')) {
        msg = msg.substring(2)
      }
      // 解密操作
      return sm2.doDecrypt(msg, privateKey, this.cipherMode)
    } catch (error) {
      console.log(error)
      // 异常处理
      return ''
    }
  }

  /**
   * 使用 SM2 解密，输出数组
   * @param privateKey - 私钥
   * @param msg - 加密后的消息
   */
  static deSm2Arr = (privateKey: string, msg: string): Uint8Array => {
    return sm2.doDecrypt(msg, privateKey, this.cipherMode, { output: 'array' })
  }

  /**
   * 使用 SM4 加密
   * @param msg - 消息
   * @param key - 密钥
   */
  static enSm4 = (msg: string, key: string): string => {
    return sm4.encrypt(msg, key)
  }

  /**
   * 使用 SM4 解密
   * @param msg - 加密后的消息
   * @param key - 密钥
   */
  static deSm4 = (msg: string, key: string): string => {
    return sm4.decrypt(msg, key)
  }
}
