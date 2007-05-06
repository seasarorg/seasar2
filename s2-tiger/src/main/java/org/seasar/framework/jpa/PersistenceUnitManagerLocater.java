package org.seasar.framework.jpa;


/**
 * 永続ユニットマネージャを取得するためのロケータです。
 * 
 * @author koichik
 */
public class PersistenceUnitManagerLocater {

    /** 永続ユニットマネージャ */
    protected static PersistenceUnitManager instance;

    /**
     * 永続ユニットマネージャを返します。
     * 
     * @return 永続ユニットマネージャ
     */
    public static PersistenceUnitManager getInstance() {
        return instance;
    }

    /**
     * 永続ユニットマネージャを設定します。
     * 
     * @param instance
     *            永続ユニットマネージャ
     */
    public static void setInstance(final PersistenceUnitManager instance) {
        PersistenceUnitManagerLocater.instance = instance;
    }

}