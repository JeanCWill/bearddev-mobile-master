package com.beardev.findrestaurant.realm;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class GenericDao<T extends RealmObject, U> {
    private String TAG;

    private Realm mRealm;
    private Class<T> persistentClass;

    private Class<U> voClass;

    public Realm getmRealm() {
        return mRealm;
    }

    public GenericDao(Class classT, Class classU) {
        this.persistentClass = classT;
        this.voClass = classU;
        TAG = this.persistentClass.getSimpleName();

        mRealm = Realm.getDefaultInstance();
    }

    public Integer getNextPrimaryKeyValue() {
        try {
            return mRealm.where(persistentClass).max("id").intValue() + 1;
        } catch(Exception e) {
            return 1;
        }
    }


    public T getEntityForUpdateById(int id) {
        return mRealm.copyFromRealm(getEntityById(id));
    }

    public T getEntityById(int id) {
        return mRealm.where(persistentClass)
                .equalTo("id", id)
                .findFirst();
    }

    public String getEntityJson(T entity) {
        return new Gson().toJson(mRealm.copyFromRealm(entity));
    }

    public U getVoById(Integer id) {
        return new Gson().fromJson(this.getEntityJson(this.getEntityById(id)), voClass);
    }

    public RealmResults<T> getAllResults() {
        return mRealm.where(persistentClass)
                .findAll();
    }

    public List<U> getAllVos() {
        List<U> restaurants = new ArrayList<>();
        RealmResults<T> registros;
        try {
            registros = this.getAllResults();

            for (T entity : registros) {
                restaurants.add(new Gson().fromJson(this.getEntityJson(this.getEntityById((Integer) entity.getClass().getMethod("getId").invoke(entity))), voClass));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        };

        return restaurants;
    }


    public List<U> getVosByIds(List<Integer> ids) {
        List<U> entitys = new ArrayList<>();
        try {
            for (Integer id : ids) {
                entitys.add(new Gson().fromJson(this.getEntityJson(this.getEntityById(id)), voClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        };

        return entitys;
    }

    public void createOrUpdateFromJson(String json) {
        mRealm.beginTransaction();
        mRealm.createOrUpdateObjectFromJson(persistentClass, json);
        mRealm.commitTransaction();
    }

    public void createOrUpdateFromJsonArray(String json) {
        mRealm.beginTransaction();
        mRealm.createOrUpdateAllFromJson(persistentClass, json);
        mRealm.commitTransaction();
    }

    /**
     * Limpa a tabela da classe em questão
     * @return
     */
    public boolean deleteAll() {
        try {
            mRealm.beginTransaction();
            mRealm.delete(persistentClass);
            mRealm.commitTransaction();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteById(int id) {
        try {
            mRealm.beginTransaction();
            mRealm.where(persistentClass).equalTo("id", id).findFirst().deleteFromRealm();
            mRealm.commitTransaction();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean deleteAllFromEntities(List<T> entities) {
        try {
            RealmQuery query = mRealm.where(persistentClass);

            for (T entity: entities) {
                query = query.equalTo("id", (Integer) entity.getClass().getMethod("getId").invoke(entity));
            }

            mRealm.beginTransaction();
            query.findAll().deleteAllFromRealm();
            mRealm.commitTransaction();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean deleteAllFromVos(List<U> vos) {
        try {

            mRealm.beginTransaction();
            for (U vo: vos) {
                mRealm.where(persistentClass).equalTo("id",  (Integer) vo.getClass().getMethod("getId").invoke(vo)).findFirst().deleteFromRealm();
            }
            mRealm.commitTransaction();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
