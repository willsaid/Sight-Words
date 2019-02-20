package com.hearatale.sightwords.ui.sight_word;

import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;

import java.util.List;

public interface ISightWordPresenter {
    List<SightWordModel> getSightWords(@SightWordsCategoryDef int category);
}
