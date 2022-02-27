package de.banarnia.api.skulls;

import org.bukkit.inventory.ItemStack;

/* Skull Enum
 * Beinhaltet alle Standard-Skulls und deren URLs.
 */
public enum Skulls {

    // Skull, für einzigartige Namen
    ID("99a2205554f4fd8431cca34da9f4d9862058488bbc4b0d6719432d110c53a07d"),

    // Skull, falls eine URL nicht gefunden wurde
    SkullNotFound("46ba63344f49dd1c4f5488e926bf3d9e2b29916a6c50d610bb40a5273dc8c82"),

    // Alphabet - Stein
    Stein_0("55a224807693978ed834355f9e5145f9c56ef68cf6f2c9e1734a46e246aae1"),
    Stein_1("31a9463fd3c433d5e1d9fec6d5d4b09a83a970b0b74dd546ce67a73348caab"),
    Stein_2("acb419d984d8796373c9646233c7a02664bd2ce3a1d3476dd9b1c5463b14ebe"),
    Stein_3("f8ebab57b7614bb22a117be43e848bcd14daecb50e8f5d0926e4864dff470"),
    Stein_4("62bfcfb489da867dce96e3c3c17a3db7c79cae8ac1f9a5a8c8ac95e4ba3"),
    Stein_5("ef4ecf110b0acee4af1da343fb136f1f2c216857dfda6961defdbee7b9528"),
    Stein_6("f331a6a6fcd6995b62088d353bfb68d9b89ae258325caf3f2886464f54a7329"),
    Stein_7("d4ba6ac07d422377a855793f36dea2ed240223f52fd1648181612ecd1a0cfd5"),
    Stein_8("c61a8a641437be9aea207253dd3f25440d954ea2b5866c552f386b29ac4d049"),
    Stein_9("a1928e1bfd86a9b79397c4cb4b65ef99af49b7d5f7957ad62c0c699a622cfbe"),
    Stein_A("2ac58b1a3b53b9481e317a1ea4fc5eed6bafca7a25e741a32e4e3c2841278c"),
    Stein_B("d4c711571e7e214ee78dfe4ee0e1263b92516e418de8fc8f3257ae0901431"),
    Stein_C("fff5aabead6feafaaecf4422cdd7837cbb36b03c9841dd1b1d2d3edb7825e851"),
    Stein_D("893e622b581975792f7c119ec6f40a4f16e552bb98776b0c7ae2bdfd4154fe7"),
    Stein_E("a157d65b19921c760ff4910b3404455b9c2ee36afc202d8538baefec676953"),
    Stein_F("c54cf261b2cd6ab54b0c624f8f6ff565a7b63e28e3b50c6dbfb52b5f0d7cf9f"),
    Stein_G("d3c9f8a74ca01ba8c54de1edc82e1fc07a83923e66574b6ffe606919240c6"),
    Stein_H("f8c58c509034617bf81ee0db9be0ba3e85ca15568163914c87669edb2fd7"),
    Stein_I("4246323c9fb319326ee2bf3f5b63ec3d99df76a12439bf0b4c3ab32d13fd9"),
    Stein_J("c58456cd9bb8a7e978591ae0cb26af1aadad4fa7a16725b295145e09bed8064"),
    Stein_K("af49fb708369e7bc2944ad706963fb6ac6ce6d4c67081ddadecfe5da51"),
    Stein_L("8c84f75416e853a74f6c70fc7e1093d53961879955b433bd8c7c6d5a6df"),
    Stein_M("31fde91b19b9309913724fea9e85311271c67bcb78578d461bf65d9613074"),
    Stein_N("1c7c972e6785d6b0aceb779abdd7702d98341c24c2a71e702930eca58055"),
    Stein_O("8073bb44f9345f9bb31a679027e7939e461842a8c27486d7a6b842c39eb38c4e"),
    Stein_P("64b231a8d55870cfb5a9f4e65db06dd7f8e34282f1416f95878b19acc34ac95"),
    Stein_Q("ffedd6f9efdb156b86935699b2b4834df0f5d214513c01d38af3bd031cbcc92"),
    Stein_R("c03a1cd583cbbffde08f943e56ac3e3afafecaede834221a81e6db6c64667f7d"),
    Stein_S("b6572e655725d78375a9817eb9ee8b37829ca1fea93b6095cc7aa19e5eac"),
    Stein_T("708c9ef3a3751e254e2af1ad8b5d668ccf5c6ec3ea2641877cba575807d39"),
    Stein_U("55a6e3ae5ae625923524838fac9fef5b42527f5027c9ca149e6c207792eb"),
    Stein_V("975121f7d9c68da0e5b6a96ac615298b12b2ee5bd19989436ee647879da5b"),
    Stein_W("67e165c3edc5541d4654c4728871e6908f613fc0ec46e823c96eac82ac62e62"),
    Stein_X("1919d1594bf809db7b44b3782bf90a69f449a87ce5d18cb40eb653fdec2722"),
    Stein_Y("e35424bb86305d7747604b13e924d74f1efe38906e4e458dd18dcc67b6ca48"),
    Stein_Z("4e91200df1cae51acc071f85c7f7f5b8449d39bb32f363b0aa51dbc85d133e"),
    Stein_Ä("4c9c2bbd7b7f7204dceb5729a6fba7fd45d6f193f3760ec59a6807533e63b"),
    Stein_Ö("c83d42bcb9b8e66c16166ccf261e2f9f78c68ee7886da225e43895cdbcaf5f"),
    Stein_Ü("caec53e4a6d221afd7297b65e55be87913cf9cb7f4f4547f7186120701d8d"),

    // Alphabet - Holz
    Holz_0("0ebe7e5215169a699acc6cefa7b73fdb108db87bb6dae2849fbe24714b27"),
    Holz_1("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530"),
    Holz_2("4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847"),
    Holz_3("1d4eae13933860a6df5e8e955693b95a8c3b15c36b8b587532ac0996bc37e5"),
    Holz_4("d2e78fb22424232dc27b81fbcb47fd24c1acf76098753f2d9c28598287db5"),
    Holz_5("6d57e3bc88a65730e31a14e3f41e038a5ecf0891a6c243643b8e5476ae2"),
    Holz_6("334b36de7d679b8bbc725499adaef24dc518f5ae23e716981e1dcc6b2720ab"),
    Holz_7("6db6eb25d1faabe30cf444dc633b5832475e38096b7e2402a3ec476dd7b9"),
    Holz_8("59194973a3f17bda9978ed6273383997222774b454386c8319c04f1f4f74c2b5"),
    Holz_9("e67caf7591b38e125a8017d58cfc6433bfaf84cd499d794f41d10bff2e5b840"),
    Holz_A("a67d813ae7ffe5be951a4f41f2aa619a5e3894e85ea5d4986f84949c63d7672e"),
    Holz_B("50c1b584f13987b466139285b2f3f28df6787123d0b32283d8794e3374e23"),
    Holz_C("abe983ec478024ec6fd046fcdfa4842676939551b47350447c77c13af18e6f"),
    Holz_D("3193dc0d4c5e80ff9a8a05d2fcfe269539cb3927190bac19da2fce61d71"),
    Holz_E("dbb2737ecbf910efe3b267db7d4b327f360abc732c77bd0e4eff1d510cdef"),
    Holz_F("b183bab50a3224024886f25251d24b6db93d73c2432559ff49e459b4cd6a"),
    Holz_G("1ca3f324beeefb6a0e2c5b3c46abc91ca91c14eba419fa4768ac3023dbb4b2"),
    Holz_H("31f3462a473549f1469f897f84a8d4119bc71d4a5d852e85c26b588a5c0c72f"),
    Holz_I("46178ad51fd52b19d0a3888710bd92068e933252aac6b13c76e7e6ea5d3226"),
    Holz_J("3a79db9923867e69c1dbf17151e6f4ad92ce681bcedd3977eebbc44c206f49"),
    Holz_K("9461b38c8e45782ada59d16132a4222c193778e7d70c4542c9536376f37be42"),
    Holz_L("319f50b432d868ae358e16f62ec26f35437aeb9492bce1356c9aa6bb19a386"),
    Holz_M("49c45a24aaabf49e217c15483204848a73582aba7fae10ee2c57bdb76482f"),
    Holz_N("35b8b3d8c77dfb8fbd2495c842eac94fffa6f593bf15a2574d854dff3928"),
    Holz_O("d11de1cadb2ade61149e5ded1bd885edf0df6259255b33b587a96f983b2a1"),
    Holz_P("a0a7989b5d6e621a121eedae6f476d35193c97c1a7cb8ecd43622a485dc2e912"),
    Holz_Q("43609f1faf81ed49c5894ac14c94ba52989fda4e1d2a52fd945a55ed719ed4"),
    Holz_R("a5ced9931ace23afc351371379bf05c635ad186943bc136474e4e5156c4c37"),
    Holz_S("3e41c60572c533e93ca421228929e54d6c856529459249c25c32ba33a1b1517"),
    Holz_T("1562e8c1d66b21e459be9a24e5c027a34d269bdce4fbee2f7678d2d3ee4718"),
    Holz_U("607fbc339ff241ac3d6619bcb68253dfc3c98782baf3f1f4efdb954f9c26"),
    Holz_V("cc9a138638fedb534d79928876baba261c7a64ba79c424dcbafcc9bac7010b8"),
    Holz_W("269ad1a88ed2b074e1303a129f94e4b710cf3e5b4d995163567f68719c3d9792"),
    Holz_X("5a6787ba32564e7c2f3a0ce64498ecbb23b89845e5a66b5cec7736f729ed37"),
    Holz_Y("c52fb388e33212a2478b5e15a96f27aca6c62ac719e1e5f87a1cf0de7b15e918"),
    Holz_Z("90582b9b5d97974b11461d63eced85f438a3eef5dc3279f9c47e1e38ea54ae8d"),
    Holz_Ä("15cd6db9ec3c7d9113e6dd49a16f99a326b9f594ce987f919559ac7dbd3b555"),
    Holz_Ö("6edbb44c639b95308ffcdf8c4770dfe8b02d752dec4b3196f4a8f9ac2315393a"),
    Holz_Ü("7b52b94c6516cbe461fea621d316cee0b875f0fbc239d25273e824b613e73dd4"),

    // Erde
    Erde_Kiste("fc27b387c33e08f9d26dd5c8ce4eba71d96e08d95e71edddc1406472a034"),

    // Pfeile
    Stein_Pfeil_Links("bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477"),
    Stein_Pfeil_Rechts("f2f3a2dfce0c3dab7ee10db385e5229f1a39534a8ba2646178e37c4fa93b"),
    Stein_Pfeil_Oben("58fe251a40e4167d35d081c27869ac151af96b6bd16dd2834d5dc7235f47791d"),
    Stein_Pfeil_Unten("9b7ce683d0868aa4378aeb60caa5ea80596bcffdab6b5af2d12595837a84853"),

    Holz_Pfeil_Links("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9"),
    Holz_Pfeil_Rechts("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf"),
    Holz_Pfeil_Oben("3040fe836a6c2fbd2c7a9c8ec6be5174fddf1ac20f55e366156fa5f712e10"),
    Holz_Pfeil_Unten("7437346d8bda78d525d19f540a95e4e79daeda795cbc5a13256236312cf");

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // URL des Heads
    private String defaultUrl;

    // Konstruktor
    Skulls(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Head bekommen
    public ItemStack get() {
        return SkullManager.getInstance().getSkullItem(getUrl());
    }

    // Aktuelle URL bekommen
    public String getUrl() {
        // Skull aufrufen
        Skull skull = SkullManager.getInstance().getSkull(toString());

        // Wenn Skull != null ist, wird die URL wiedergegeben
        if (skull != null)
            return skull.getUrl();

        // Wenn Skull == null, wird die Standard-URL wiedergegeben
        return defaultUrl;
    }

    // URL bekommen
    public String getDefaultUrl() {
        return defaultUrl;
    }

    // Name bekommen
    public String getName() {
        return this.toString();
    }

}
