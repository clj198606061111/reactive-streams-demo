# reactive-streams-demo
reactive-streams-demo

## 概念

响应式流(Reactive Streams)是以带非阻塞背压方式处理异步数据流的标准，提供一组最小化的接口，方法和协议来描述必要的操作和实体。

## 要解决的问题

系统之间高并发的大量数据流交互通常采用异步的发布-订阅模式。数据由发布者推送给订阅者的过程中，容易产生的一个问题是，当发布者即生产者产生的数据速度远远大于订阅者即消费者的消费速度时，消费者会承受巨大的资源压力(pressure)而有可能崩溃。

## 解决原理

为了解决以上问题，数据流的速度需要被控制，即流量控制(flow control)，以防止快速的数据流不会压垮目标。因此需要反压即背压(back pressure)，生产者和消费者之间需要通过实现一种背压机制来互操作。实现这种背压机制要求是异步非阻塞的，如果是同步阻塞的，消费者在处理数据时生产者必须等待，会产生性能问题。

## 解决方法

响应式流(Reactive Streams)通过定义一组实体，接口和互操作方法，给出了实现非阻塞背压的标准。第三方遵循这个标准来实现具体的解决方案，常见的有Reactor，RxJava，Akka Streams，Ratpack等。

该标准定义了四个接口

### 发布者Publisher

```java
interface Publisher<T> {
 void subscribe(Subscriber<? super T> subscriber);
}
```

发布者只有一个方法，用来接受订阅者进行订阅(subscribe)。T代表发布者和订阅者之间传输的数据类型。

### 订阅者Subscriber

```java
interface Subscriber<T> {
 void onSubscribe(Subscription s);
 void onNext(T t);
 void onError(Throwable t);
 void onComplete();
}
```

订阅者有四个事件方法，分别在开启订阅、接收数据、发生错误和数据传输结束时被调用。

### 订阅对象Subscription

```java
interface Subscription {
 void request(long n);
 void cancel();
}
```

订阅对象是发布者和订阅者之间交互的操作对象，在发布者(Publisher)通过subscribe方法加入订阅者时，会通过调用订阅者(Subscriber)的onSubscribe把订阅对象(Subscription)传给订阅者。

订阅者拿到订阅对象后，通过调用订阅对象的request方法，根据自身消费能力请求n条数据，或者调用cancel方法来停止接收数据。

订阅对象的request方法被调用时，会触发订阅者的onNext事件方法，把数据传输给订阅者。如果数据全部传输完成，则触发订阅者的onComplete事件方法。如果数据传输发生错误，则触发订阅者的onError事件方法。

### 处理者Processor

```java
interface Processor<T,R> extends Subscriber<T>, Publisher<R> {
}
```

处理者既是发布者又是订阅者，用于在发布者和订阅者之间转换数据格式，把发布者的T类型数据转换为订阅者接受的R类型数据。处理者作为数据转换的中介不是必须的。

由以上的接口可以看出，核心在于订阅者可以通过request(long n)方法来控制接收的数据量，达到了实现背压的目的。

----
参考
- https://blog.csdn.net/wudaoshihun/article/details/83070086
- https://www.jianshu.com/p/9d3a2a28976a