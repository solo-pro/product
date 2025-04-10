package com.ecommer.product.auth;

import ch.qos.logback.core.subst.Token;
import com.google.common.util.concurrent.ListenableFuture;
import io.ecommer.grpc.TokenRequest;
import io.ecommer.grpc.UserGrpc;
import io.ecommer.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthGrpcServiceImpl implements ReactiveUserDetailsService {
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.just(getUser(username));
    }
    @GrpcClient("auth-grpc-server")
    private UserGrpc.UserBlockingStub userBlockingStub;

    public User getUser(String token) {
        UserResponse user = userBlockingStub.getUser(TokenRequest.newBuilder().setToken(token).build());
        return User.from(user);
    }
}
