package br.com.angelorobson.alternativescene.application.partials.abount


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import com.vansuita.materialabout.builder.AboutBuilder
import kotlinx.android.synthetic.main.about_fragment.*


class AboutFragment : FragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbarWithDisplayArrowBack(getString(R.string.about))
        hideBottomNavigation()
        setUpAboutScreen()
    }


    private fun setUpAboutScreen() {
        val frameLayout = about

        val builder = AboutBuilder.with(requireContext())
            .setAppIcon(R.mipmap.ic_launcher)
            .setAppName(R.string.app_name)
            .setPhoto(R.drawable.angelo)
            .setCover(R.mipmap.profile_cover)
            .setLinksAnimated(true)
            .setDividerDashGap(13)
            .setName("Ângelo Robson")
            .setSubTitle("Engenheiro de Software")
            .setLinksColumnsCount(4)
            .setBrief("Sou um amante e um entusiasta da tecnologia. Fabricante de idéias e curioso.")
            .addGitHubLink("angelorobsonmelo")
            .addLinkedInLink("ângelo-melo-8b4a47148/")
            .addEmailLink("angelorobsonmelo@gmail.com")
            .addWhatsappLink("Ângelo Robson", "+5582991228122")
            .addFiveStarsAction()
            .setVersionNameAsAppSubTitle()
            .addShareAction(R.string.app_name)
            .setActionsColumnsCount(2)
            .setWrapScrollView(true)
            .setShowAsCard(true)

        val view = builder.build()

        frameLayout.addView(view)

        val item = builder.lastLinkId

        view.findItem(item).setOnClickListener {
            val url =
                "https://api.whatsapp.com/send?phone=5582991228122&text=OL%C3%A1%2C%20te%20encontrei%20atrav%C3%A9s%20do%20aplicativo%20Eventos%20Alternativos"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }


}
