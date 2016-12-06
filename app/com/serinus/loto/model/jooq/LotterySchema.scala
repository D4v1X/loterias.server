/**
 * This class is generated by jOOQ
 */
package com.serinus.loto.model.jooq


import com.serinus.loto.model.jooq.tables.TmCombinationPart
import com.serinus.loto.model.jooq.tables.TmLottery
import com.serinus.loto.model.jooq.tables.TwResult

import java.util.ArrayList
import java.util.Arrays
import java.util.List

import javax.annotation.Generated

import org.jooq.Catalog
import org.jooq.Sequence
import org.jooq.Table
import org.jooq.impl.SchemaImpl

import scala.Array


object LotterySchema {

  /**
   * The reference instance of <code>lottery_schema</code>
   */
  val LOTTERY_SCHEMA = new LotterySchema
}

/**
 * This class is generated by jOOQ.
 */
@Generated(
  value = Array(
    "http://www.jooq.org",
    "jOOQ version:3.8.6"
  ),
  comments = "This class is generated by jOOQ"
)
class LotterySchema extends SchemaImpl("lottery_schema", DefaultCatalog.DEFAULT_CATALOG) {

  override def getCatalog : Catalog = DefaultCatalog.DEFAULT_CATALOG

  override def getSequences : List[Sequence[_]] = {
    val result = new ArrayList[Sequence[_]]
    result.addAll(getSequences0)
    result
  }

  private def getSequences0() : List[Sequence[_]] = {
    return Arrays.asList[Sequence[_]](
      Sequences.TM_COMBINATION_PART_ID_SEQ,
      Sequences.TM_LOTTERY_ID_SEQ,
      Sequences.TW_RESULT_ID_SEQ)
  }

  override def getTables : List[Table[_]] = {
    val result = new ArrayList[Table[_]]
    result.addAll(getTables0)
    result
  }

  private def getTables0() : List[Table[_]] = {
    return Arrays.asList[Table[_]](
      TmCombinationPart.TM_COMBINATION_PART,
      TmLottery.TM_LOTTERY,
      TwResult.TW_RESULT)
  }
}
