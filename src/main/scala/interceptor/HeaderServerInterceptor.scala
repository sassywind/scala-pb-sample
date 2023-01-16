package interceptor

import io.grpc.{
  ForwardingServerCall,
  Metadata,
  ServerCall,
  ServerCallHandler,
  ServerInterceptor
}

import java.util.logging.Logger

object HeaderServerInterceptor {

  private val logger: Logger =
    Logger.getLogger(classOf[HeaderServerInterceptor].getName)

  private val customHeadKey: Metadata.Key[String] = Metadata.Key.of(
    "custom_server_header_key",
    Metadata.ASCII_STRING_MARSHALLER
  )
}

class HeaderServerInterceptor extends ServerInterceptor {
  def interceptCall[ReqT, RespT](
      call: ServerCall[ReqT, RespT],
      headers: Metadata,
      next: ServerCallHandler[ReqT, RespT]
  ): ServerCall.Listener[ReqT] = {
    HeaderServerInterceptor.logger.info(
      s"header received from client: ${headers.toString}"
    )
    next.startCall(
      new ForwardingServerCall.SimpleForwardingServerCall[ReqT, RespT](call) {
        override def sendHeaders(responseHeaders: Metadata): Unit = {
          responseHeaders
            .put(
              HeaderServerInterceptor.customHeadKey,
              headers.get(HeaderServerInterceptor.customHeadKey)
            )
          super.sendHeaders(responseHeaders)
        }
      },
      headers
    )
  }
}
