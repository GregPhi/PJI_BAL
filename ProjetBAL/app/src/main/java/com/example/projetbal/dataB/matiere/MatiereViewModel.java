package com.example.projetbal.dataB.matiere;

import android.app.Application;

import com.example.projetbal.object.book.Matiere;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MatiereViewModel extends AndroidViewModel {

    private MatiereRepository mRepository;

    private LiveData<List<Matiere>> mAllMatieres;

    public MatiereViewModel(Application application) {
        super(application);
        mRepository = new MatiereRepository(application);
        mAllMatieres = mRepository.getmAllMatieres();
    }

    public LiveData<List<Matiere>> getmAllMatieres() { return mAllMatieres; }

    public List<Matiere> getmAllMatieresForSpinner(){ return mRepository.getmAllMatieresForSpinner(); }

    public List<String> getmAllMatieresNameForSpinner(){ return mRepository.getmAllMatieresNameForSpinner(); }

    public void insert(Matiere matiere) { mRepository.insert(matiere); }

    public void updateBook(Matiere... matiere){
        mRepository.updateBook(matiere[0]);
    }

    public void delete(Matiere matiere) { mRepository.delete(matiere); }

    public void deleteAll(){ mRepository.deleteAll(); }


}
