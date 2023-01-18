package interceptor

import interceptor.TimestampInterceptor.{
  timestampContextKey,
  timestampMetadataKey
}
import io.grpc.{
  Context,
  Contexts,
  Metadata,
  ServerCall,
  ServerCallHandler,
  ServerInterceptor
}

import java.util.logging.Logger

object TimestampInterceptor {

  private val logger: Logger =
    Logger.getLogger(classOf[TimestampInterceptor].getName)

  val timestampContextKey: Context.Key[String] =
    Context.key("timestamp")

  private val timestampMetadataKey: Metadata.Key[String] = Metadata.Key.of(
    "timestamp",
    Metadata.ASCII_STRING_MARSHALLER
  )
}

class TimestampInterceptor extends ServerInterceptor {
  def interceptCall[ReqT, RespT](
      call: ServerCall[ReqT, RespT],
      headers: Metadata,
      next: ServerCallHandler[ReqT, RespT]
  ): ServerCall.Listener[ReqT] = {
    TimestampInterceptor.logger.info(
      s"header received from client: ${headers.toString}"
    )

    val ctx = Context.current.withValue(
      timestampContextKey,
      headers.get(timestampMetadataKey)
    )
    Contexts.interceptCall(
      ctx,
      call,
      headers,
      next
    )
  }
}
