package com.helloclient.grpc.user.service;

import com.helloclient.grpc.user.pojo.User;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.0)",
    comments = "Source: User.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<User.UserRequest,
      User.UserResponse> getFindUserInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findUserInfo",
      requestType = User.UserRequest.class,
      responseType = User.UserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<User.UserRequest,
      User.UserResponse> getFindUserInfoMethod() {
    io.grpc.MethodDescriptor<User.UserRequest, User.UserResponse> getFindUserInfoMethod;
    if ((getFindUserInfoMethod = UserServiceGrpc.getFindUserInfoMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getFindUserInfoMethod = UserServiceGrpc.getFindUserInfoMethod) == null) {
          UserServiceGrpc.getFindUserInfoMethod = getFindUserInfoMethod =
              io.grpc.MethodDescriptor.<User.UserRequest, User.UserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findUserInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("findUserInfo"))
              .build();
        }
      }
    }
    return getFindUserInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<User.UserFindRequest,
      User.UserFindResponse> getFindUserByCityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findUserByCity",
      requestType = User.UserFindRequest.class,
      responseType = User.UserFindResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<User.UserFindRequest,
      User.UserFindResponse> getFindUserByCityMethod() {
    io.grpc.MethodDescriptor<User.UserFindRequest, User.UserFindResponse> getFindUserByCityMethod;
    if ((getFindUserByCityMethod = UserServiceGrpc.getFindUserByCityMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getFindUserByCityMethod = UserServiceGrpc.getFindUserByCityMethod) == null) {
          UserServiceGrpc.getFindUserByCityMethod = getFindUserByCityMethod =
              io.grpc.MethodDescriptor.<User.UserFindRequest, User.UserFindResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findUserByCity"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserFindRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserFindResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("findUserByCity"))
              .build();
        }
      }
    }
    return getFindUserByCityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<User.UserInsertRequest,
      User.UserInsertResponse> getInsertUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "insertUser",
      requestType = User.UserInsertRequest.class,
      responseType = User.UserInsertResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<User.UserInsertRequest,
      User.UserInsertResponse> getInsertUserMethod() {
    io.grpc.MethodDescriptor<User.UserInsertRequest, User.UserInsertResponse> getInsertUserMethod;
    if ((getInsertUserMethod = UserServiceGrpc.getInsertUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getInsertUserMethod = UserServiceGrpc.getInsertUserMethod) == null) {
          UserServiceGrpc.getInsertUserMethod = getInsertUserMethod =
              io.grpc.MethodDescriptor.<User.UserInsertRequest, User.UserInsertResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "insertUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserInsertRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserInsertResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("insertUser"))
              .build();
        }
      }
    }
    return getInsertUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<User.UserFilterRequest,
      User.UserFilterResponse> getListerUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "listerUser",
      requestType = User.UserFilterRequest.class,
      responseType = User.UserFilterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<User.UserFilterRequest,
      User.UserFilterResponse> getListerUserMethod() {
    io.grpc.MethodDescriptor<User.UserFilterRequest, User.UserFilterResponse> getListerUserMethod;
    if ((getListerUserMethod = UserServiceGrpc.getListerUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getListerUserMethod = UserServiceGrpc.getListerUserMethod) == null) {
          UserServiceGrpc.getListerUserMethod = getListerUserMethod =
              io.grpc.MethodDescriptor.<User.UserFilterRequest, User.UserFilterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "listerUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserFilterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  User.UserFilterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("listerUser"))
              .build();
        }
      }
    }
    return getListerUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceStub>() {
        @java.lang.Override
        public UserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceStub(channel, callOptions);
        }
      };
    return UserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub>() {
        @java.lang.Override
        public UserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingStub(channel, callOptions);
        }
      };
    return UserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub>() {
        @java.lang.Override
        public UserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceFutureStub(channel, callOptions);
        }
      };
    return UserServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     *查找用户信息
     * </pre>
     */
    default void findUserInfo(User.UserRequest request,
        io.grpc.stub.StreamObserver<User.UserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindUserInfoMethod(), responseObserver);
    }

    /**
     * <pre>
     *通过城市查询 每查询到一个就返回一个
     * </pre>
     */
    default void findUserByCity(User.UserFindRequest request,
        io.grpc.stub.StreamObserver<User.UserFindResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindUserByCityMethod(), responseObserver);
    }

    /**
     * <pre>
     *插入多个user 最后返回总的结果
     * </pre>
     */
    default io.grpc.stub.StreamObserver<User.UserInsertRequest> insertUser(
        io.grpc.stub.StreamObserver<User.UserInsertResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getInsertUserMethod(), responseObserver);
    }

    /**
     * <pre>
     *服务端有新增的user就推送给客户端， filter可以根据实际情况来选择筛选条件
     * </pre>
     */
    default io.grpc.stub.StreamObserver<User.UserFilterRequest> listerUser(
        io.grpc.stub.StreamObserver<User.UserFilterResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getListerUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserService.
   */
  public static abstract class UserServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserService.
   */
  public static final class UserServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UserServiceStub> {
    private UserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *查找用户信息
     * </pre>
     */
    public void findUserInfo(User.UserRequest request,
        io.grpc.stub.StreamObserver<User.UserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindUserInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *通过城市查询 每查询到一个就返回一个
     * </pre>
     */
    public void findUserByCity(User.UserFindRequest request,
        io.grpc.stub.StreamObserver<User.UserFindResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getFindUserByCityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *插入多个user 最后返回总的结果
     * </pre>
     */
    public io.grpc.stub.StreamObserver<User.UserInsertRequest> insertUser(
        io.grpc.stub.StreamObserver<User.UserInsertResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getInsertUserMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *服务端有新增的user就推送给客户端， filter可以根据实际情况来选择筛选条件
     * </pre>
     */
    public io.grpc.stub.StreamObserver<User.UserFilterRequest> listerUser(
        io.grpc.stub.StreamObserver<User.UserFilterResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getListerUserMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserService.
   */
  public static final class UserServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *查找用户信息
     * </pre>
     */
    public User.UserResponse findUserInfo(User.UserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindUserInfoMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *通过城市查询 每查询到一个就返回一个
     * </pre>
     */
    public java.util.Iterator<User.UserFindResponse> findUserByCity(
        User.UserFindRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getFindUserByCityMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserService.
   */
  public static final class UserServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserServiceFutureStub> {
    private UserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *查找用户信息
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<User.UserResponse> findUserInfo(
        User.UserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindUserInfoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_USER_INFO = 0;
  private static final int METHODID_FIND_USER_BY_CITY = 1;
  private static final int METHODID_INSERT_USER = 2;
  private static final int METHODID_LISTER_USER = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FIND_USER_INFO:
          serviceImpl.findUserInfo((User.UserRequest) request,
              (io.grpc.stub.StreamObserver<User.UserResponse>) responseObserver);
          break;
        case METHODID_FIND_USER_BY_CITY:
          serviceImpl.findUserByCity((User.UserFindRequest) request,
              (io.grpc.stub.StreamObserver<User.UserFindResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INSERT_USER:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.insertUser(
              (io.grpc.stub.StreamObserver<User.UserInsertResponse>) responseObserver);
        case METHODID_LISTER_USER:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.listerUser(
              (io.grpc.stub.StreamObserver<User.UserFilterResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getFindUserInfoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              User.UserRequest,
              User.UserResponse>(
                service, METHODID_FIND_USER_INFO)))
        .addMethod(
          getFindUserByCityMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              User.UserFindRequest,
              User.UserFindResponse>(
                service, METHODID_FIND_USER_BY_CITY)))
        .addMethod(
          getInsertUserMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              User.UserInsertRequest,
              User.UserInsertResponse>(
                service, METHODID_INSERT_USER)))
        .addMethod(
          getListerUserMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              User.UserFilterRequest,
              User.UserFilterResponse>(
                service, METHODID_LISTER_USER)))
        .build();
  }

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return User.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getFindUserInfoMethod())
              .addMethod(getFindUserByCityMethod())
              .addMethod(getInsertUserMethod())
              .addMethod(getListerUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
