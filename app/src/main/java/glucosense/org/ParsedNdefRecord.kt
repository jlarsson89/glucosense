package glucosense.org

import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
import android.view.View


/**
 * Created by johan on 26/02/18.
 */
interface ParsedNdefRecord {

    /**
     * Returns a view to display this record.
     */
    fun getView(activity: Activity, inflater: LayoutInflater, parent: ViewGroup,
                offset: Int): View

}