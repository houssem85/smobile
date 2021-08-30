package fr.strada.smobile.ui.gerer_absence

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*


class TabFragmentAdapter (fm: FragmentManager ) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var position : Int = 0

    val fragmentList: MutableList<Fragment> = ArrayList()
    val fragmentTitleList: MutableList<String> = ArrayList()

    private var baseId: Long = 0

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(i: Int): Fragment {
        position = i
        return fragmentList[i]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment?, title: String) {

        fragmentList.add(fragment!!)
        fragmentTitleList.add(title)
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        // give an ID different from position when position has been changed
        this.position= position
        return baseId + position
    }

    /**
     * Notify that the position of a fragment has been changed.
     * Create a new ID for each position to force recreation of the fragment
     * @param n number of items which have been changed
     */
    fun notifyChangeInPosition(n: Int) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += count + n.toLong()
    }

}