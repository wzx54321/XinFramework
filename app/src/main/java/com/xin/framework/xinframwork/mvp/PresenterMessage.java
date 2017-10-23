package com.xin.framework.xinframwork.mvp;

import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;

/**
 * Description :  MVP 中用于解耦合copy from Android Message。
 * Created by xin on 2017/8/18 0018.
 */

public class PresenterMessage implements Parcelable {
    /**
     * User-defined PresenterMessage code so that the recipient can identify
     * what this PresenterMessage is about. Each {@link IView} has its own name-space
     * for PresenterMessage codes, so you do not need to worry about yours conflicting
     * with other IViews.
     */
    public int what;

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #setData(Bundle) setData()} if you only need to store a
     * few integer values.
     */
    public int arg1;

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #setData(Bundle) setData()} if you only need to store a
     * few integer values.
     */
    public int arg2;

    /**
     * An arbitrary object to send to the recipient.  When using
     * {@link Messenger} to send the PresenterMessage across processes this can only
     * be non-null if it contains a Parcelable of a framework class (not one
     * implemented by the application).   For other data transfer use
     * {@link #setData}.
     * <p>
     * <p>Note that Parcelable objects here are not supported prior to
     * the {@link android.os.Build.VERSION_CODES#FROYO} release.
     */
    public Object obj;

    /**
     * Optional Messenger where replies to this PresenterMessage can be sent.  The
     * semantics of exactly how this is used are up to the sender and
     * receiver.
     */
    public Messenger replyTo;

    /**
     * Optional field indicating the uid that sent the PresenterMessage.  This is
     * only valid for PresenterMessages posted by a {@link Messenger}; otherwise,
     * it will be -1.
     */
    public int sendingUid = -1;

    /**
     * If set PresenterMessage is in use.
     * This flag is set when the PresenterMessage is enqueued and remains set while it
     * is delivered and afterwards when it is recycled.  The flag is only cleared
     * when a new PresenterMessage is created or obtained since that is the only time that
     * applications are allowed to modify the contents of the PresenterMessage.
     * <p>
     * It is an error to attempt to enqueue or recycle a PresenterMessage that is already in use.
     */
    /*package*/ static final int FLAG_IN_USE = 1;

    /**
     * If set PresenterMessage is asynchronous
     */
    /*package*/ static final int FLAG_ASYNCHRONOUS = 1 << 1;

    /**
     * Flags to clear in the copyFrom method
     */
    /*package*/ static final int FLAGS_TO_CLEAR_ON_COPY_FROM = FLAG_IN_USE;

    /*package*/ int flags;

    /*package*/ long when;

    /*package*/ Bundle data;

    /*package*/ IView target;

    /*package*/ Runnable callback;

    // sometimes we store linked lists of these things
    /*package*/ PresenterMessage next;
    /**
     * 充当锁的作用，避免多线程争抢资源，导致脏数据
     */
    private static final Object sPoolSync = new Object();
    /**
     * 消息队列的头部的指针
     */
    private static PresenterMessage sPool;
    /**
     * 当前的消息队列的长度
     */
    private static int sPoolSize = 0;
    /**
     * 定义消息队列缓存消息的最大的长度
     */
    private static final int MAX_POOL_SIZE = 50;

    private static boolean gCheckRecycle = true;

    /**
     * Return a new PresenterMessage instance from the global pool. Allows us to
     * avoid allocating new objects in many cases.
     */
    public static PresenterMessage obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {//判断当前的队列的指针是否为空
                PresenterMessage m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new PresenterMessage();
    }

    /**
     * Same as {@link #obtain()}, but copies the values of an existing
     * PresenterMessage (including its target) into the new one.
     *
     * @param orig Original PresenterMessage to copy.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(PresenterMessage orig) {
        PresenterMessage m = obtain();
        m.what = orig.what;
        m.arg1 = orig.arg1;
        m.arg2 = orig.arg2;
        m.obj = orig.obj;
        m.replyTo = orig.replyTo;
        m.sendingUid = orig.sendingUid;
        if (orig.data != null) {
            m.data = new Bundle(orig.data);
        }
        m.target = orig.target;
        m.callback = orig.callback;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the value for the <em>target</em> member on the PresenterMessage returned.
     *
     * @param h IView to assign to the returned PresenterMessage object's <em>target</em> member.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(IView h) {
        PresenterMessage m = obtain();
        m.target = h;

        return m;
    }

    /**
     * Same as {@link #obtain(IView)}, but assigns a callback Runnable on
     * the PresenterMessage that is returned.
     *
     * @param h        IView to assign to the returned PresenterMessage object's <em>target</em> member.
     * @param callback Runnable that will execute when the PresenterMessage is handled.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(IView h, Runnable callback) {
        PresenterMessage m = obtain();
        m.target = h;
        m.callback = callback;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values for both <em>target</em> and
     * <em>what</em> members on the PresenterMessage.
     *
     * @param h    Value to assign to the <em>target</em> member.
     * @param what Value to assign to the <em>what</em> member.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(IView h, int what) {
        PresenterMessage m = obtain();
        m.target = h;
        m.what = what;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values of the <em>target</em>, <em>what</em>, and <em>obj</em>
     * members.
     *
     * @param h    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param obj  The <em>object</em> method to set.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(IView h, int what, Object obj) {
        PresenterMessage m = obtain();
        m.target = h;
        m.what = what;
        m.obj = obj;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values of the <em>target</em>, <em>what</em>,
     * <em>arg1</em>, and <em>arg2</em> members.
     *
     * @param h    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param arg1 The <em>arg1</em> value to set.
     * @param arg2 The <em>arg2</em> value to set.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(IView h, int what, int arg1, int arg2) {
        PresenterMessage m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values of the <em>target</em>, <em>what</em>,
     * <em>arg1</em>, <em>arg2</em>, and <em>obj</em> members.
     *
     * @param h    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param arg1 The <em>arg1</em> value to set.
     * @param arg2 The <em>arg2</em> value to set.
     * @param obj  The <em>obj</em> value to set.
     * @return A PresenterMessage object from the global pool.
     */
    public static PresenterMessage obtain(IView h, int what,
                                          int arg1, int arg2, Object obj) {
        PresenterMessage m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.obj = obj;

        return m;
    }


    /**
     * Return a PresenterMessage instance to the global pool.
     * <p>
     * You MUST NOT touch the PresenterMessage after calling this function because it has
     * effectively been freed.  It is an error to recycle a PresenterMessage that is currently
     * enqueued or that is in the process of being delivered to a IView.
     * </p>
     */
    public void recycle() {
        if (isInUse()) {
            if (gCheckRecycle) {
                throw new IllegalStateException("This PresenterMessage cannot be recycled because it "
                        + "is still in use.");
            }
            return;
        }
        recycleUnchecked();
    }

    /**
     * Recycles a PresenterMessage that may be in-use.
     * Used internally by the PresenterMessageQueue and Looper when disposing of queued PresenterMessages.
     */
    void recycleUnchecked() {
        // Mark the PresenterMessage as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        replyTo = null;
        sendingUid = -1;
        when = 0;
        target = null;
        callback = null;
        data = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }

    /**
     * Make this PresenterMessage like o.  Performs a shallow copy of the data field.
     * Does not copy the linked list fields, nor the timestamp or
     * target/callback of the original PresenterMessage.
     */
    public void copyFrom(PresenterMessage o) {
        this.flags = o.flags & ~FLAGS_TO_CLEAR_ON_COPY_FROM;
        this.what = o.what;
        this.arg1 = o.arg1;
        this.arg2 = o.arg2;
        this.obj = o.obj;
        this.replyTo = o.replyTo;
        this.sendingUid = o.sendingUid;

        if (o.data != null) {
            this.data = (Bundle) o.data.clone();
        } else {
            this.data = null;
        }
    }

    /**
     * Return the targeted delivery time of this PresenterMessage, in milliseconds.
     */
    public long getWhen() {
        return when;
    }

    public void setTarget(IView target) {
        this.target = target;
    }

    /**
     * Retrieve the a {@link com.xin.framework.xinframwork.mvp.IView IView} implementation that
     * will receive this PresenterMessage. The object must implement
     * {@link com.xin.framework.xinframwork.mvp.IView#handlePresenterMsg(PresenterMessage)} (com.xin.framework.xinframwork.mvp.PresenterMessage)
     * IView.handlePresenterMsg}. Each IView has its own name-space for
     * PresenterMessage codes, so you do not need to
     * worry about yours conflicting with other IViews.
     */
    public IView getTarget() {
        return target;
    }

    /**
     * Retrieve callback object that will execute when this PresenterMessage is handled.
     * This object must implement Runnable. This is called by
     * the <em>target</em> {@link IView} that is receiving this PresenterMessage to
     * dispatch it.  If
     * not set, the PresenterMessage will be dispatched to the receiving IView's
     * {@link IView#handlePresenterMsg(PresenterMessage)} (PresenterMessage IView.handlePresenterMessage())}.
     */
    public Runnable getCallback() {
        return callback;
    }

    /**
     * Obtains a Bundle of arbitrary data associated with this
     * event, lazily creating it if necessary. Set this value by calling
     * {@link #setData(Bundle)}.  Note that when transferring data across
     * processes via {@link Messenger}, you will need to set your ClassLoader
     * on the Bundle via {@link Bundle#setClassLoader(ClassLoader)
     * Bundle.setClassLoader()} so that it can instantiate your objects when
     * you retrieve them.
     *
     * @see #peekData()
     * @see #setData(Bundle)
     */
    public Bundle getData() {
        if (data == null) {
            data = new Bundle();
        }

        return data;
    }

    /**
     * Like getData(), but does not lazily create the Bundle.  A null
     * is returned if the Bundle does not already exist.  See
     * {@link #getData} for further information on this.
     *
     * @see #getData()
     * @see #setData(Bundle)
     */
    public Bundle peekData() {
        return data;
    }

    /**
     * Sets a Bundle of arbitrary data values. Use arg1 and arg2 members
     * as a lower cost way to send a few simple integer values, if you can.
     *
     * @see #getData()
     * @see #peekData()
     */
    public void setData(Bundle data) {
        this.data = data;
    }

    /**
     * Sends this PresenterMessage to the IView specified by {@link #getTarget}.
     * Throws a null pointer exception if this field has not been set.
     */
    public void sendToTarget() {
        target.handlePresenterMsg(this);
    }


    /*package*/ boolean isInUse() {
        return ((flags & FLAG_IN_USE) == FLAG_IN_USE);
    }

    /*package*/ void markInUse() {
        flags |= FLAG_IN_USE;
    }

    /**
     * Constructor (but the preferred way to get a PresenterMessage is to call {@link #obtain() PresenterMessage.obtain()}).
     */
    public PresenterMessage() {
    }

    @Override
    public String toString() {
        return toString(SystemClock.uptimeMillis());
    }

    String toString(long now) {
        StringBuilder b = new StringBuilder();
        b.append("{ when=");
        TimeUtils.formatDuration(when - now, b);

        if (target != null) {
            if (callback != null) {
                b.append(" callback=");
                b.append(callback.getClass().getName());
            } else {
                b.append(" what=");
                b.append(what);
            }

            if (arg1 != 0) {
                b.append(" arg1=");
                b.append(arg1);
            }

            if (arg2 != 0) {
                b.append(" arg2=");
                b.append(arg2);
            }

            if (obj != null) {
                b.append(" obj=");
                b.append(obj);
            }

            b.append(" target=");
            b.append(target.getClass().getName());
        } else {
            b.append(" barrier=");
            b.append(arg1);
        }

        b.append(" }");
        return b.toString();
    }

    public static final Parcelable.Creator<PresenterMessage> CREATOR
            = new Parcelable.Creator<PresenterMessage>() {
        public PresenterMessage createFromParcel(Parcel source) {
            PresenterMessage msg = PresenterMessage.obtain();
            msg.readFromParcel(source);
            return msg;
        }

        public PresenterMessage[] newArray(int size) {
            return new PresenterMessage[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (callback != null) {
            throw new RuntimeException(
                    "Can't marshal callbacks across processes.");
        }
        dest.writeInt(what);
        dest.writeInt(arg1);
        dest.writeInt(arg2);
        if (obj != null) {
            try {
                Parcelable p = (Parcelable) obj;
                dest.writeInt(1);
                dest.writeParcelable(p, flags);
            } catch (ClassCastException e) {
                throw new RuntimeException(
                        "Can't marshal non-Parcelable objects across processes.");
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeLong(when);
        dest.writeBundle(data);
        Messenger.writeMessengerOrNullToParcel(replyTo, dest);
        dest.writeInt(sendingUid);
    }

    private void readFromParcel(Parcel source) {
        what = source.readInt();
        arg1 = source.readInt();
        arg2 = source.readInt();
        if (source.readInt() != 0) {
            obj = source.readParcelable(getClass().getClassLoader());
        }
        when = source.readLong();
        data = source.readBundle(getClass().getClassLoader());
        replyTo = Messenger.readMessengerOrNullFromParcel(source);
        sendingUid = source.readInt();
    }
}
