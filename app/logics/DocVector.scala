package logics

import java.text.Normalizer
import org.atilika.kuromoji.Tokenizer
import org.atilika.kuromoji.Token
import scala.collection.immutable.Set

object DocVector {

  sealed abstract class Document
  case class RawDocument(guid: Long, body: String) extends Document
  case class DocumentVector(guid: Long, body: String, tfidf: TFIDF) extends Document

  type TF = Map[String, Int] // term -> term frequency
  type DF = Map[String, Int] // term -> document frequency
  type TFIDF = Map[String, Double] // term -> tfidf

  def tf(document: RawDocument): Map[String, Int] = tf(document.body)

  def tf(document: String): TF =
    MorphoAnalysis.tokenize(document)
      .filter(MorphoAnalysis.isUsefull)
      .map(_.getBaseForm).filter(_ != null) // why java...
      .groupBy(_.toString).mapValues(_.length)

  def dfs(tfs: List[TF]): DF =
    tfs.flatMap(_.keys).distinct.foldLeft[Map[String, Int]](Map()) { (m, term) =>
      m + (
        term -> tfs.foldLeft[Int](0)((m, tf) => m + (if (tf.isDefinedAt(term)) 1 else 0))
      )
    }

  // n: the number of Documents
  def idf(term: String, dfs: DF, n: Int): Double =
    dfs.get(term).fold[Double](0)(df => scala.math.log(n.toDouble / df.toDouble))

  def tfidf(documents: List[RawDocument]): List[DocumentVector] = {
    val tfs: List[(RawDocument, TF)] = documents.map(doc => (doc, tf(doc)))
    val dfMap = dfs(tfs map (_._2))
    tfs map {
      case (doc, tf) =>
        val tfidf = tf.foldLeft[Map[String, Double]](Map()) { (m, tuple) => // tf value
          val (term: String, tfv: Int) = tuple
          m + (term -> tfv * idf(term, dfMap, documents.length))
        }
        DocumentVector(guid = doc.guid, body = doc.body, tfidf = tfidf)
    }
  }

  // Cosine similarity
  def simcos(a: DocumentVector, b: DocumentVector): Double =
    product(a, b) / (math.sqrt(product(a, a)) * math.sqrt(product(b, b)))

  def findSimilarDocs(doc: DocumentVector, docs: List[DocumentVector], n: Int = 1): List[DocumentVector] =
    docs.par.withFilter(d => d.guid != doc.guid).map { d =>
      (d, simcos(doc, d))
    }.toList.sortBy(-_._2).map(_._1).take(n)

  def product(a: DocumentVector, b: DocumentVector): Double =
    a.tfidf.foldLeft[Double](0) { (v, t) =>
      val (term: String, tfidf: Double) = t
      val other = b.tfidf.get(term).getOrElse[Double](0)
      v + (tfidf * other)
    }

}

object MorphoAnalysis {
  import scala.collection.JavaConversions._

  def tokenize(text: String): List[Token] =
    tokenizer.tokenize(unicodeNormalize(text)).toList

  private val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build

  private def partOfSpeech(token: Token) = token.getAllFeaturesArray().head

  def isUsefull(token: Token): Boolean =
    (Set("名詞", "動詞") contains partOfSpeech(token)) && !(stopwords contains token.getSurfaceForm)

  private def unicodeNormalize = (str: String) => Normalizer.normalize(str, Normalizer.Form.NFKC)

  // http://svn.sourceforge.jp/svnroot/slothlib/CSharp/Version1/SlothLib/NLP/Filter/StopWord/word/Japanese.txt
  val stopwords = Set(
    "あそこ",
    "あたり",
    "あちら",
    "あっち",
    "あと",
    "あな",
    "あなた",
    "あれ",
    "いくつ",
    "いつ",
    "いま",
    "いや",
    "いろいろ",
    "うち",
    "おおまか",
    "おまえ",
    "おれ",
    "がい",
    "かく",
    "かたち",
    "かやの",
    "から",
    "がら",
    "きた",
    "くせ",
    "ここ",
    "こっち",
    "こと",
    "ごと",
    "こちら",
    "ごっちゃ",
    "これ",
    "これら",
    "ごろ",
    "さまざま",
    "さらい",
    "さん",
    "しかた",
    "しよう",
    "すか",
    "ずつ",
    "すね",
    "すべて",
    "ぜんぶ",
    "そう",
    "そこ",
    "そちら",
    "そっち",
    "そで",
    "それ",
    "それぞれ",
    "それなり",
    "たくさん",
    "たち",
    "たび",
    "ため",
    "だめ",
    "ちゃ",
    "ちゃん",
    "てん",
    "とおり",
    "とき",
    "どこ",
    "どこか",
    "ところ",
    "どちら",
    "どっか",
    "どっち",
    "どれ",
    "なか",
    "なかば",
    "なに",
    "など",
    "なん",
    "はじめ",
    "はず",
    "はるか",
    "ひと",
    "ひとつ",
    "ふく",
    "ぶり",
    "べつ",
    "へん",
    "ぺん",
    "ほう",
    "ほか",
    "まさ",
    "まし",
    "まとも",
    "まま",
    "みたい",
    "みつ",
    "みなさん",
    "みんな",
    "もと",
    "もの",
    "もん",
    "やつ",
    "よう",
    "よそ",
    "わけ",
    "わたし",
    "ハイ",
    "上",
    "中",
    "下",
    "字",
    "年",
    "月",
    "日",
    "時",
    "分",
    "秒",
    "週",
    "火",
    "水",
    "木",
    "金",
    "土",
    "国",
    "都",
    "道",
    "府",
    "県",
    "市",
    "区",
    "町",
    "村",
    "各",
    "第",
    "方",
    "何",
    "的",
    "度",
    "文",
    "者",
    "性",
    "体",
    "人",
    "他",
    "今",
    "部",
    "課",
    "係",
    "外",
    "類",
    "達",
    "気",
    "室",
    "口",
    "誰",
    "用",
    "界",
    "会",
    "首",
    "男",
    "女",
    "別",
    "話",
    "私",
    "屋",
    "店",
    "家",
    "場",
    "等",
    "見",
    "際",
    "観",
    "段",
    "略",
    "例",
    "系",
    "論",
    "形",
    "間",
    "地",
    "員",
    "線",
    "点",
    "書",
    "品",
    "力",
    "法",
    "感",
    "作",
    "元",
    "手",
    "数",
    "彼",
    "彼女",
    "子",
    "内",
    "楽",
    "喜",
    "怒",
    "哀",
    "輪",
    "頃",
    "化",
    "境",
    "俺",
    "奴",
    "高",
    "校",
    "婦",
    "伸",
    "紀",
    "誌",
    "レ",
    "行",
    "列",
    "事",
    "士",
    "台",
    "集",
    "様",
    "所",
    "歴",
    "器",
    "名",
    "情",
    "連",
    "毎",
    "式",
    "簿",
    "回",
    "匹",
    "個",
    "席",
    "束",
    "歳",
    "目",
    "通",
    "面",
    "円",
    "玉",
    "枚",
    "前",
    "後",
    "左",
    "右",
    "次",
    "先",
    "春",
    "夏",
    "秋",
    "冬",
    "一",
    "二",
    "三",
    "四",
    "五",
    "六",
    "七",
    "八",
    "九",
    "十",
    "百",
    "千",
    "万",
    "億",
    "兆",
    "下記",
    "上記",
    "時間",
    "今回",
    "前回",
    "場合",
    "一つ",
    "年生",
    "自分",
    "ヶ所",
    "ヵ所",
    "カ所",
    "箇所",
    "ヶ月",
    "ヵ月",
    "カ月",
    "箇月",
    "名前",
    "本当",
    "確か",
    "時点",
    "全部",
    "関係",
    "近く",
    "方法",
    "我々",
    "違い",
    "多く",
    "扱い",
    "新た",
    "その後",
    "半ば",
    "結局",
    "様々",
    "以前",
    "以後",
    "以降",
    "未満",
    "以上",
    "以下",
    "幾つ",
    "毎日",
    "自体",
    "向こう",
    "何人",
    "手段",
    "同じ",
    "感じ"
  )

}
