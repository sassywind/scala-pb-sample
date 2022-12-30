import io.grpc.{Server, ServerBuilder}
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
      .addService(GreeterGrpc.bindService(new GreeterImpl, executionContext))
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
