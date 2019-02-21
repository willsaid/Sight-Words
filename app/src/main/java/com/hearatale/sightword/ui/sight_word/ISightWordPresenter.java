package com.hearatale.sightword.ui.sight_word;

import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.data.model.typedef.SightWordsCategoryDef;

import java.util.List;

public interface ISightWordPresenter {
    List<SightWordModel> getSightWords(@SightWordsCategoryDef int category);
}
