/**
 * This class is generated by jOOQ
 */
package com.serinus.loto.model.jooq.tables


import com.serinus.loto.model.converters.TimestampToLocalDateTimeConverter
import com.serinus.loto.model.jooq.Keys
import com.serinus.loto.model.jooq.LotterySchema
import com.serinus.loto.model.jooq.tables.records.TmCombinationPartRecord

import java.lang.Class
import java.lang.Integer
import java.lang.Short
import java.lang.String
import java.time.LocalDateTime
import java.util.Arrays
import java.util.List

import javax.annotation.Generated

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UniqueKey
import org.jooq.impl.TableImpl

import scala.Array


object TmCombinationPart {

  /**
   * The reference instance of <code>lottery_schema.tm_combination_part</code>
   */
  val TM_COMBINATION_PART = new TmCombinationPart
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
class TmCombinationPart(alias : String, aliased : Table[TmCombinationPartRecord], parameters : Array[ Field[_] ]) extends TableImpl[TmCombinationPartRecord](alias, LotterySchema.LOTTERY_SCHEMA, aliased, parameters, "") {

  /**
   * The class holding records for this type
   */
  override def getRecordType : Class[TmCombinationPartRecord] = {
    classOf[TmCombinationPartRecord]
  }

  /**
   * The column <code>lottery_schema.tm_combination_part.id</code>.
   */
  val ID : TableField[TmCombinationPartRecord, Integer] = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('lottery_schema.tm_combination_part_id_seq'::regclass)", org.jooq.impl.SQLDataType.INTEGER)), "")

  /**
   * The column <code>lottery_schema.tm_combination_part.lottery_id</code>.
   */
  val LOTTERY_ID : TableField[TmCombinationPartRecord, Integer] = createField("lottery_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

  /**
   * The column <code>lottery_schema.tm_combination_part.part_number</code>.
   */
  val PART_NUMBER : TableField[TmCombinationPartRecord, Short] = createField("part_number", org.jooq.impl.SQLDataType.SMALLINT.nullable(false), "")

  /**
   * The column <code>lottery_schema.tm_combination_part.name</code>.
   */
  val NAME : TableField[TmCombinationPartRecord, String] = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(30).nullable(false), "")

  /**
   * The column <code>lottery_schema.tm_combination_part.num_values</code>.
   */
  val NUM_VALUES : TableField[TmCombinationPartRecord, Short] = createField("num_values", org.jooq.impl.SQLDataType.SMALLINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("1", org.jooq.impl.SQLDataType.SMALLINT)), "")

  /**
   * The column <code>lottery_schema.tm_combination_part.created</code>.
   */
  val CREATED : TableField[TmCombinationPartRecord, LocalDateTime] = createField("created", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.TIMESTAMP)), "", new TimestampToLocalDateTimeConverter())

  /**
   * The column <code>lottery_schema.tm_combination_part.modified</code>.
   */
  val MODIFIED : TableField[TmCombinationPartRecord, LocalDateTime] = createField("modified", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.TIMESTAMP)), "", new TimestampToLocalDateTimeConverter())

  /**
   * Create a <code>lottery_schema.tm_combination_part</code> table reference
   */
  def this() = {
    this("tm_combination_part", null, null)
  }

  /**
   * Create an aliased <code>lottery_schema.tm_combination_part</code> table reference
   */
  def this(alias : String) = {
    this(alias, com.serinus.loto.model.jooq.tables.TmCombinationPart.TM_COMBINATION_PART, null)
  }

  private def this(alias : String, aliased : Table[TmCombinationPartRecord]) = {
    this(alias, aliased, null)
  }

  override def getSchema : Schema = LotterySchema.LOTTERY_SCHEMA

  override def getIdentity : Identity[TmCombinationPartRecord, Integer] = {
    Keys.IDENTITY_TM_COMBINATION_PART
  }

  override def getPrimaryKey : UniqueKey[TmCombinationPartRecord] = {
    Keys.TM_COMBINATION_PART_PK
  }

  override def getKeys : List[ UniqueKey[TmCombinationPartRecord] ] = {
    return Arrays.asList[ UniqueKey[TmCombinationPartRecord] ](Keys.TM_COMBINATION_PART_PK, Keys.TM_COMBINATION_PART_UK)
  }

  override def getReferences : List[ ForeignKey[TmCombinationPartRecord, _] ] = {
    return Arrays.asList[ ForeignKey[TmCombinationPartRecord, _] ](Keys.TM_COMBINATION_PART__TM_COMBINATION_PART_TM_LOTTERY_FK)
  }

  override def as(alias : String) : TmCombinationPart = {
    new TmCombinationPart(alias, this)
  }

  /**
   * Rename this table
   */
  def rename(name : String) : TmCombinationPart = {
    new TmCombinationPart(name, null)
  }
}
