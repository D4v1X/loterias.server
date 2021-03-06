/**
 * This class is generated by jOOQ
 */
package com.serinus.loto.model.jooq.tables.daos


import com.serinus.loto.model.jooq.tables.TmLottery
import com.serinus.loto.model.jooq.tables.records.TmLotteryRecord

import java.lang.Integer
import java.lang.String
import java.time.LocalDateTime
import java.util.List

import javax.annotation.Generated

import org.jooq.Configuration
import org.jooq.impl.DAOImpl

import scala.Array


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
class TmLotteryDao(configuration : Configuration) extends DAOImpl[TmLotteryRecord, com.serinus.loto.model.pojos.TmLottery, Integer](TmLottery.TM_LOTTERY, classOf[com.serinus.loto.model.pojos.TmLottery], configuration) {

  /**
   * Create a new TmLotteryDao without any configuration
   */
  def this() = {
    this(null)
  }

  override protected def getId(o : com.serinus.loto.model.pojos.TmLottery) : Integer = {
    o.getId
  }

  /**
   * Fetch records that have <code>id IN (values)</code>
   */
  def fetchById(values : Integer*) : List[com.serinus.loto.model.pojos.TmLottery] = {
    fetch(TmLottery.TM_LOTTERY.ID, values:_*)
  }

  /**
   * Fetch a unique record that has <code>id = value</code>
   */
  def fetchOneById(value : Integer) : com.serinus.loto.model.pojos.TmLottery = {
    fetchOne(TmLottery.TM_LOTTERY.ID, value)
  }

  /**
   * Fetch records that have <code>name IN (values)</code>
   */
  def fetchByName(values : String*) : List[com.serinus.loto.model.pojos.TmLottery] = {
    fetch(TmLottery.TM_LOTTERY.NAME, values:_*)
  }

  /**
   * Fetch records that have <code>created IN (values)</code>
   */
  def fetchByCreated(values : LocalDateTime*) : List[com.serinus.loto.model.pojos.TmLottery] = {
    fetch(TmLottery.TM_LOTTERY.CREATED, values:_*)
  }

  /**
   * Fetch records that have <code>modified IN (values)</code>
   */
  def fetchByModified(values : LocalDateTime*) : List[com.serinus.loto.model.pojos.TmLottery] = {
    fetch(TmLottery.TM_LOTTERY.MODIFIED, values:_*)
  }
}
