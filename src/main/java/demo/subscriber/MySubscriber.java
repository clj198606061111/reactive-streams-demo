package demo.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MySubscriber<T> implements Subscriber<T> {
    private Subscription subscription;

    public void onSubscribe(Subscription s) {
        this.subscription = subscription;
        subscription.request(1);
    }

    public void onNext(T t) {
        System.out.println("Received: " + t);
        subscription.request(1);
    }

    public void onError(Throwable t) {
        t.printStackTrace();
        synchronized ("A") {
            "A".notifyAll();
        }
    }

    public void onComplete() {
        System.out.println("Done");
        synchronized ("A") {
            "A".notifyAll();
        }
    }
}
