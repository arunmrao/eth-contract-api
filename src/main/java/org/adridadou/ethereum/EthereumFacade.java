package org.adridadou.ethereum;

import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * Created by davidroon on 31.03.16.
 * This code is released under Apache 2 license
 */
public class EthereumFacade {
    private final EthereumContractInvocationHandler handler;
    private final BlockchainProxy blockchainProxy;

    public EthereumFacade(EthereumContractInvocationHandler handler, BlockchainProxy blockchainProxy) {
        this.handler = handler;
        this.blockchainProxy = blockchainProxy;
    }

    public <T> T createContractProxy(String code, EthAddress address, Class<T> contractInterface) throws IOException {
        handler.register(contractInterface, code, address);
        return (T) Proxy.newProxyInstance(contractInterface.getClassLoader(), new Class[]{contractInterface}, handler);
    }

    public EthAddress publishContract(String code) {
        return blockchainProxy.publish(code);
    }

    public boolean isSyncDone() {
        return blockchainProxy.isSyncDone();
    }

    public void waitForSyncDone() throws InterruptedException {
        while (!isSyncDone()) {
            synchronized (this) {
                wait(200);
            }
        }
    }

    public EthAddress getSenderAddress() {
        return blockchainProxy.getSenderAddress();
    }
}
