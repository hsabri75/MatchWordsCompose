package com.example.matchwordscompose.source

import com.example.matchwords.mvc.model.source.ISource
import com.example.wordselect.io.TextConverter

class BulmacaSource: ISource {

    override fun getSourceData(): Array<Array<String>> {
        val src =
            "Ayça//Hilal\nHarman makinesi//Patoz\nYol//Tarik\nİstihsal//Üretim\nKaftan//Hilat\nHalı tezgahı//Istar\nAltınkökü//İpeka\nAilesine bakan//Ail\nDağ kirlangici//Ebabil\nYıldırım//Saika\nTelli balikcil//Okar\nHitabet sanatı//Retorik\nYardım//İane\nMantık//Eseme\nAşı boyası//Okr\nNazar otu//Üzerlik\nBaş çoban//Eke\nRafadan//Alakok\nSardalya yavrusu//Papalina\nAkciğer//Rie\nEğrelti//Fujer\nGölge//Saye\nLokmanruhu//Eter\nEşit, denk//Müsavi\nCüzzam//Lepra\nDahi//Öke\nBel kemiği//Oma\nKuyruksokumu//Uca\nCezire//Ada\nYemislerin yenen bölümü//Eten\nToplardamar//Vena\nAtar damar//Arter\nSüpürge otu//Erika\nParafin//Alkan\nKapan//Fak\nSebep//Saik\nDikili taş//Obelisk\nKoni//Mahrut\nSıtma ilacı//Kinin\nAsma karpuz dalı//Tevek\nAsalak//Ekti\nİstimbot//Çatana\nEnerji//Erke\nTirsi//Alosa\nOk kılıfı//Sadak\nYarı//Nim\nHint bademi//Kakao\nAkıtma//İsale\nKıyye//Okka\nBunama//Ateh\nYükün//İyon\nGumus baligi küçüğü//İlarya\nAntidot//Panzehir\nBüyük zoka//Sinara\nJapon imparatoru//Mikado\nBağırsaklar//Ema\nGemi aşırtma havuzu//Lok\nDağ servisi//Arar\nÇok küçük taneli fasulye//Cilban\nKonsolos//Şehbender\nElebaşı//Sergerde\nArapların taktiği çember//Agel\nYer fıstığı//Araşit\nYörünge//Mahrek\nZaviye//Açı\nHükümdar yönetimindeki halk//Reaya\nKongo antilobu//Okapi\nKarasal//Berri\nVeba//Taun\nKus yemi//Dane\nKöpek maması//Yal\nYalan söyleme hastalığı//Mitomani\nPekmez toprağı//Marn\nHastalık nöbeti//Akse\nİşlenmiş hayvan derisi//Kösele\nBağırsak//Mia\nHayvan iğdiş etme//Eneme\nKopça halkası//Brit\nKatarakt//Akbasma\nMermer//Oniks\nBoyun eğen//Ram\nLimonlu//Ser\nAv//Şikar\nKüçük saray//Kasr\nDevlet hazinesi//Beytülmal\nGözyaşı//Eşk\nGeçerli//Meri\nGece bekçisi//Ases\nAsur başkenti//Ninova\nHabeş soylusu//Ras\nSınır taşı//Ura\nEski türklerde doktor//Atasagun\nDivit, yazı hokkasi//Ame\nKement//Laso\nGemiyi karaya bağlama//Abaşo\nTabaklanmis ceylan derisi//Rak\nSusamış//Nai\nParaguay çayı//Mate\nAlkol//Etanol\nMasif//Som\nOngun//Arma\nPadişah ahırına bakan//İmrahor\nReçine//Akma\nPalmiye türü//Areka\nYaban mersini türü//Enir\nSarp geçit//Akabe\nKreme yakın beyaz//Ekru\nEğme//İmale\nGizli yer//Tun\nNişasta bulamacı//Lapa\nBazı bitkilerin öz suyu//Lateks\nSatürn uydusu//Rea\nKalın dar tahta//Lata\nGece//Şeb\nYemek//Naam\nKurbağa//Anura\nEş, zevce//Refika\nAkaju//Maun\nTahıl ambarı//Silo\nSakağı//Ruam\nTırpana//Rina\nSoğurma//Mas\nRapor//Yazanak\nYüz, sima//Ru\nLesotho//Ls\nHavaci bülteni//Notam\nMezbaha//Kanara\nKertenkele//Elöpen\nRessam sehpası//Şövale\nSu samuru//Lütr\nYatay//Ufki\nVolkanik patlama çukuru//Maar\nKalede ok atma deliği//Barbakan\nYün egirme değneği//Öreke\nDoğaçlama//Tuluat\nYayın//Neşriyat\nKira//İcar\nİçten çürüyen ağaç//Ardak\nKulak iltihabı//Otit\nAsit//Hamız\nBoşanma//Talak\nNoter//Katibiadil\nPlasenta//Etene\nBalta//Teber\nMenzil//Erim\nSık gözlü ağ//Tor\nTahta ayakkabı//Sabo\nHamur yaprağı//Yufka\nSanayii//Uran\nDişi geyik//Maral\nMermer yapıştırıcı//Akemi\nKalıcı//Payidar\nHırvatistan para birimi//Kuna\nTutuk dilli//Pepe\nAlacak//Takanak\nPolen//Tal\nÖykünce//Fabl\nŞömine//Peç\nFrengi//Şankr\nAsma yaprağı//Bat\nSomurtkan//Abus\nTorbalı balık ağı//Trata\nAlay, İstihza//Saraka\nKıbrıs eski adı//Alaşiya\nÖküz yemliği//Akere\nYaba//Dirgen, Anadut\nJapon türküsü//Uta\nArk//Karık, Dren\nKardinal başlığı//Barata\nMeksika parası//Peso\nMerkür//Utarit\nKumtaşı//Gre\nMide iltihabı//Gastrit\nTozluk türü//Tetr\nMavera//Öte\nVali//İlbay\nCüzzamlı//Alaten\nAykırı, Karşıt//Hilaf\nAlbay//Miralay\nEş//Ayal\nBir kara yumuşakçası//Ena\n"

        return TextConverter.toArray(src)
    }


}