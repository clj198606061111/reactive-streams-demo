package demo;

import demo.subscriber.MySubscriber;

import java.util.Arrays;

public class FlowDemo {
    public static void main(String[] args) {
        // Create a publisher.

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        // Create a subscriber and register it with the publisher.

        MySubscriber<String> subscriber = new MySubscriber<>();
        publisher.subscribe(subscriber);

        // Publish several data items and then close the publisher.

        System.out.println("Publishing data items...");
        String[] items = { "jan", "feb", "mar", "apr", "may", "jun",
                "jul", "aug", "sep", "oct", "nov", "dec" };
        Arrays.asList(items).stream().forEach(i -> publisher.submit(i));
        publisher.close();

        try
        {
            synchronized("A")
            {
                "A".wait();
            }
        }
        catch (InterruptedException ie)
        {
        }
    }
}
