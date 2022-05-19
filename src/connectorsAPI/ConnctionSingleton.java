/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package connectorsAPI;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author doron
 */
public class ConnctionSingleton {

    private EntityManagerFactory emf;

    private ConnctionSingleton() {

        this.emf = Persistence.createEntityManagerFactory("MinesweeperPU");

    }

    public static ConnctionSingleton getInstance() {
        return ConnctionSingletonHolder.INSTANCE;
    }

    private static class ConnctionSingletonHolder {

        private static final ConnctionSingleton INSTANCE = new ConnctionSingleton();
    }
}
