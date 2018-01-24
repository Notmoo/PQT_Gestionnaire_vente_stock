package com.pqt.client.module.account;

import com.pqt.client.module.account.listeners.AccountListenerAdapter;
import com.pqt.client.module.account.listeners.AccountUpdateBuilder;
import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.ICollectionItemMessageCallback;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.core.entities.user_account.Account;
import com.pqt.client.module.account.listeners.IAccountListener;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

//TODO écrire javadoc
//TODO Issue #6 : add log lines
//TODO Issue #17 : supporter la modif de comptes
public class AccountService {

	private QueryExecutor executor;
	private Account currentAccount;
	private boolean connected;
	private Collection<Account> accounts;
	private EventListenerList listenerList;

	public AccountService(QueryExecutor executor){
		this.executor = executor;
		listenerList = new EventListenerList();
		accounts = new ArrayList<>();
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account account) {
        if(connected)
            logOutCurrentAccount();
	    this.currentAccount = account;
	}

	public boolean isCurrentAccountLoggedIn() {
		return connected;
	}

	public void logInCurrentAccount(String password) {
        setCurrentAccountState(password, true);
	}

	public void logOutCurrentAccount() {
        setCurrentAccountState(null, false);
	}

	private void setCurrentAccountState(String password, boolean state){
        if(currentAccount!=null && connected!=state) {
            Account acc = new Account(currentAccount);
            acc.setPassword(password);
            executor.executeConnectAccountQuery(acc, state, new INoItemMessageCallback() {
                @Override
                public void ack() {
                    if (currentAccount != null
                            && connected != state
                            && currentAccount.getUsername().equals(acc.getUsername())
                            && currentAccount.getPermissionLevel().equals(acc.getPermissionLevel())) {
                        connected = state;
                        Arrays.stream(listenerList.getListeners(IAccountListener.class))
                                .forEach(l->l.onAccountStatusChangedEvent(connected));
                    }else
                        Arrays.stream(listenerList.getListeners(IAccountListener.class))
                                .forEach(l->l.onAccountStatusNotChangedEvent(
                                        new IllegalStateException("Account service not in the right state")
                                ));
                }

                @Override
                public void err(Throwable cause) {
                    Arrays.stream(listenerList.getListeners(IAccountListener.class))
                            .forEach(l->l.onAccountStatusNotChangedEvent(cause));
                }

                @Override
                public void ref(Throwable cause) {
                    Arrays.stream(listenerList.getListeners(IAccountListener.class))
                            .forEach(l->l.onAccountStatusNotChangedEvent(cause));
                }
            });
        }
    }

	public void addListener(IAccountListener listener) {
        listenerList.add(IAccountListener.class, listener);
	}

	public void removeListener(IAccountListener listener) {
        listenerList.remove(IAccountListener.class, listener);
	}

    public Collection<Account> getAllAccounts() {
        return accounts;
    }

    public void refreshAccounts(){
	    executor.executeAccountListQuery(new ICollectionItemMessageCallback<Account>() {
            @Override
            public void err(Throwable cause) {

            }

            @Override
            public void ref(Throwable cause) {

            }

            @Override
            public void ack(Collection<Account> obj) {
                accounts = obj;
                Arrays.stream(listenerList.getListeners(IAccountListener.class))
                        .forEach(IAccountListener::onAccountListChangedEvent);
            }
        });
    }

    public void shutdown() {
        if(connected) {
            try {
                CountDownLatch latch = new CountDownLatch(1);
                new Thread(() ->{
                    this.addListener(new AccountListenerAdapter() {
                        @Override
                        public void onAccountStatusChangedEvent(boolean status) {
                            //TODO Issue #6 : ajouter des logs
                            latch.countDown();
                        }

                        @Override
                        public void onAccountStatusNotChangedEvent(Throwable cause) {
                            //TODO Issue #6 : ajouter des logs
                            cause.printStackTrace();
                            latch.countDown();
                        }
                    });
                    logOutCurrentAccount();
                }).start();
                latch.await(); // Wait for thread to call latch.countDown()
            } catch (InterruptedException e) {
                //TODO Issue #6 : ajouter des logs
                e.printStackTrace();
            }finally {
                listenerList = null;
            }
        }else{
            //Nothing to do
        }
    }

    public void submitAccountUpdate(AccountUpdateBuilder builder) {
        executor.executeAccountUpdateQuery(builder.build(), new INoItemMessageCallback() {
            @Override
            public void ack() {
                //TODO Issue #6 : add log line
                refreshAccounts();
            }

            @Override
            public void err(Throwable cause) {
                //TODO Issue #6 : add log line
            }

            @Override
            public void ref(Throwable cause) {
                //TODO Issue #6 : add log line
            }
        });
    }
}
