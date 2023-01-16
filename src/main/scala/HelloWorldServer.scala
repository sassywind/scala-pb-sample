import interceptor.HeaderServerInterceptor
import io.grpc.{Server, ServerBuilder, ServerInterceptors}
import sample.helloworld.{GreeterGrpc, HelloReply, HelloRequest}

import scala.concurrent.{ExecutionContext, Future}

object HelloWorldServer {
  def main(args: Array[String]): Unit = {
    new HelloWorldServer(ExecutionContext.global).start().awaitTermination()
  }

  private val port = 50051
}

class HelloWorldServer(executionContext: ExecutionContext) { self =>
  private def start(): Server = {
    ServerBuilder
      .forPort(HelloWorldServer.port)
      .addService(
        ServerInterceptors.intercept(
          GreeterGrpc.bindService(new GreeterImpl, executionContext),
          new HeaderServerInterceptor
        )
      )
      .build()
      .start
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(req: HelloRequest): Future[HelloReply] = {
      val reply = HelloReply(message = "Hello " + req.name)
      println(reply)
      Future.successful(reply)
    }
  }

}
