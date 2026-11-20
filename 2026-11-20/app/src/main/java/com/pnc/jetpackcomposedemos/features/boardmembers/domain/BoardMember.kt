package com.pnc.jetpackcomposedemos.features.boardmembers.domain

data class BoardMember (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val title: String,
    val imageUrl: String,
    val bio: String,
) {

    companion object {

        fun getBoardMembers(): List<BoardMember> {
            return listOf(
                BoardMember(
                    id = 101,
                    firstName = "Morgan",
                    lastName = "Sloan",
                    title = "CEO",
                    imageUrl = "/images/MorganSloan.jpg",
                    bio = "<h3>Education</h3><ul><li>Received a B.A. in Rambling from Faber College</li><li>Earned a M.A. in Dithering from Whatsamatter U.</li><li>Earned a Ph.D. in Bloviating from Pendleton State University</li></ul><h3>History/Accomplishments</h3><p>Won several awards for consulting about electric trains in Atlantic City, NJ. Have a strong interest in getting to know junk food in Mexico. Enthusiastic about developing strategies for bacon for farmers. Spent the better part of the 90's implementing soap scum in Suffolk, NY. Spent 2001-2005 consulting about Uno in the aftermarket. Spent 2002-2008 researching methane in Orlando, FL.</p>"
                ),
                BoardMember(
                    id = 102,
                    firstName = "Lamont",
                    lastName = "McRae",
                    title = "VP, Talent Acquisition",
                    imageUrl = "/images/LamontMcRae.jpg",
                    bio = "<h3>Education</h3><ul><li>Masters in Marketing from Nottingham University</li></ul><h3>History/Accomplishments</h3><p>Had  brief career investing in Elvis figurines in the financial sector. Enthusiastic about exporting carnival rides in Washington, DC. Spent several years buying and selling cod in Miami, FL. Enthusiastic about implementing inflatable dolls in Fort Lauderdale, FL. Had some great experience getting to know acne in Las Vegas, NV. Spent the better part of the 90's short selling licorice in the UK.</p>"
                ),
                BoardMember(
                    id = 103,
                    firstName = "Cody",
                    lastName = "Hester",
                    title = "Controller",
                    imageUrl = "/images/CodyHester.jpg",
                    bio = "<h3>Education</h3><ul><li>BS in Accounting from University of California, Davis</li></ul><h3>History/Accomplishments</h3><p>What gets me going now is promoting hugs with no outside help. Garnered an industry award while deploying children's books for the government. Spent 2002-2010 testing the market for dandruff in Gainesville, FL. Spent two years deploying carp in the government sector. Managed a small team building electric trains in Ocean City, NJ. Developed several new methods for donating cannibalism on Wall Street.</ p>"
                ),
                BoardMember(
                    id = 104,
                    firstName = "Laurel",
                    lastName = "Beasley",
                    title = "Chief Technical Officer",
                    imageUrl = "/images/LaurelBeasley.jpg",
                    bio = "<h3>Education</h3><ul><li>MS in Information Technology from MIT</li></ul><h3>History/Accomplishments</h3><p>Spent college summers licensing pogo sticks for the government. A real dynamo when it comes to deploying glass worldwide. Spent 2002 - 2008 exporting beauty for electricians. Spent high school summers training circus clowns in Pensacola, FL. Spent several months supervising the production of human growth hormone worldwide.</p>"
                )
            )
        }

    }


}