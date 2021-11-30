package com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners

import com.unfpa.appsistenciamaternaunfpa.models.KnowledgeablePerson

/**
 * Created by KhyatiShah on 12/9/2019.
 */
interface OnKnowledgeablePersonFetched {
   fun onComplete(lstKnowledgeablePerson: ArrayList<KnowledgeablePerson>)
}