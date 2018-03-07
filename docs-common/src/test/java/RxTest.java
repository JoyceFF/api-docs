import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class RxTest {
    public static void main(String[] args) {
        Observable<String> sender = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("create1"); //发射一个"create1"的String
                subscriber.onNext("create2"); //发射一个"create2"的String
                subscriber.onCompleted();//发射完成,这种方法需要手动调用onCompleted，才会回调Observer的onCompleted方法

            }
        });
        Observer<String> receiver = new Observer<String>() {

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
                //数据接收完成时调用
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
                //发生错误调用
            }

            @Override
            public void onNext(String s) {

                //正常接收数据调用
                System.out.println(s);  //将接收到来自sender的问候"Hi，Weavey！"
            }
        };

        sender.subscribe(receiver);
    }
}
