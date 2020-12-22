export interface GoogleCaptchaAPIResponse {
  success: boolean;
  challengeTimeStamp: Date;
  hostname: string;
  errorCodes: Array<string>;
}
