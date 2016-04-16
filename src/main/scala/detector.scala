import io.Source

// TODO: way to store dictionnary in a text file?

object Detector {

  // reading dictionnary
  def getDic = {
    val readf = Source.fromFile("src/main/dict.txt").getLines.toList map {_.split(" ").toList}
    val lang = readf.map(_.apply(0)).distinct
    lang.map((l:String)=>(l,readf filter{_(0)==l} map((lis)=>(lis(1)(0),lis(2).toDouble))toMap)).toMap
  }

  val dic = getDic
  def main(args: Array[String]) = {
    println("Starting program")
    println("Possible languages")
    dic.keys foreach println
    val text = io.StdIn.readLine
    val l = find_language(text,dic)
    print("Language: ")
    println(l)
    print("Score: ")
    println(compareFreq(dic.apply(l),countFreq(text)))
  }

  // methods building features
  // 1. simple letter freq: Map letter->frequency
  def countFreq(text: String):Map[Char,Double] = {
    val count = "abcdefghijklmnopqrtstuvwxyz".toList
    return count.map((c:Char)=>(c->text.count(_==c).toDouble/
                                   text.length.toDouble)).toMap
  }

  // Computes the score of each language for the text
  // def comp_language(text:String,countRef: Map[String, Map[Char, Double]]):
  // Iterable[String] = {
  //   val c = countFreq(text.toList.map(_.toString))
  //   return countRef map {tup => (tup,compareFreq(tup._2,c))}
  // }

  // finds the best fitting language from the frequency count and
  def find_language(text:String,countRef: Map[String, Map[Char, Double]]):
  String = {
    val c = countFreq(text)
    return countRef.filter((count)=>compareFreq(count._2,c)==
      countRef.values.map(compareFreq(_,c)).min).keys.toList.apply(0)
  }

  def compareFreq(countLang:Map[Char,Double],countText:Map[Char,Double]):
  Double = {
    return countLang.keys.map((c:Char)=>Math.abs(countLang.apply(c)-
    countText.apply(c))).sum
  }

}

// 2. letter transition frequency: Map (letter1,letter2)->frequency
// include letter transition from and to whitespace
// def countTrans(text: List[String]):Map[(Char,Char),Double] = {
//
// }

// object DicBuilder {
//
// }
