package jp.co.yumemi.android.code_check.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.co.yumemi.android.code_check.TopActivity
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TopActivityTest {

    private val hiltRule = HiltAndroidRule(this)
    private val activityRule = ActivityScenarioRule(TopActivity::class.java)

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(activityRule)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `TopActivityが起動する`() {
        // Activityが正常に起動していることを確認
        activityRule.scenario.onActivity { activity ->
            assertNotNull(activity)
            assertTrue(activity is TopActivity)
        }
    }
}
