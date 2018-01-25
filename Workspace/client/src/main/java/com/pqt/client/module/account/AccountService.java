package com.pqt.client.module.account;

import com.pqt.client.module.account.listeners.AccountListenerAdapter;
import com.pqt.client.module.account.listeners.AccountUpdateBuilder;
import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.ICollectionItemMessageCallback;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.core.entities.user_account.Account;
import com.pqt.client.module.account.listeners.IAccountListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

//TODO écrire javadoc
public class AccountService {

    private static Logger LOGGER = LogManager.getLogger(AccountService.class);

	private QueryExecutor executor;
	private Account currentAccount;
	private boolean connected;
	private Collection<Account> accounts;
	private EventListenerList listenerList;

	public AccountService(QueryExecutor executor){
        LOGGER.info("Initialisation du service 'Account'");
		this.executor = executor;
		listenerList = new EventListenerList();
		accounts = new ArrayList<>();
        LOGGER.info("Service 'Account' initialisé");
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
                        fireAccountStatusChangedEvent(connected);
                    }else
                        fireAccountStatusNotChangedEvent(
                                new IllegalStateException("Account service not in the right state")
                        );
                }

                @Override
                public void err(Throwable cause) {
                    fireAccountStatusNotChangedEvent(cause);
                }

                @Override
                public void ref(Throwable cause) {
                    fireAccountStatusNotChangedEvent(cause);
                }
            });
        }
    }

    private void fireAccountStatusChangedEvent(boolean connected){
	    LOGGER.info("Etat du compte courant changé à {}", connected);
        Arrays.stream(listenerList.getListeners(IAccountListener.class))
                .forEach(l->l.onAccountStatusChangedEvent(connected));
    }

    private void fireAccountStatusNotChangedEvent(Throwable cause){
        LOGGER.info("Etat du compte courant inchangé : {}", cause);
        Arrays.stream(listenerList.getListeners(IAccountListener.class))
                .forEach(l->l.onAccountStatusNotChangedEvent(cause));
    }

    private void fireAccountListChangedEvent(){
	    LOGGER.info("Liste des comptes utilisateurs changée");
        Arrays.stream(listenerList.getListeners(IAccountListener.class))
                .forEach(IAccountListener::onAccountListChangedEvent);
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
                fireAccountListChangedEvent();
            }
        });
    }

    public void shutdown() {
        LOGGER.info("Fermeture du service 'Account'");
        if(connected) {
            try {
                CountDownLatch latch = new CountDownLatch(1);
                new Thread(() ->{
                    this.addListener(new AccountListenerAdapter() {
                        @Override
                        public void onAccountStatusChangedEvent(boolean status) {
                            latch.countDown();
                        }

                        @Override
                        public void onAccountStatusNotChangedEvent(Throwable cause) {
                            cause.printStackTrace();
                            latch.countDown();
                        }
                    });
                    logOutCurrentAccount();
                }).start();
                latch.await(); // Wait for thread to call latch.countDown()
            } catch (InterruptedException e) {
                LOGGER.error("Interruption de la procédure de fermeture du service 'Account' : {}", e);
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
                refreshAccounts();
            }

            @Override
            public void err(Throwable cause) {
            }

            @Override
            public void ref(Throwable cause) {
            }
        });
    }
}
