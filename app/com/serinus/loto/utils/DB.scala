package com.serinus.loto.utils

import javax.inject.Inject

import akka.actor.ActorSystem
import org.jooq.{SQLDialect, DSLContext}
import org.jooq.impl.DSL
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

class DB @Inject() (db: Database, system: ActorSystem) {


  implicit val databaseContext: ExecutionContext = system.dispatchers.lookup("contexts.database")


  def query[A](queryFn: DSLContext => A): Future[A] = Future {
    db.withConnection { connection =>

      val dslCtxt = DSL.using(connection, SQLDialect.POSTGRES)

      queryFn apply dslCtxt

    }
  }


  def withTransaction[A](queryFn: DSLContext => A): Future[A] = Future {
    db.withTransaction { connection =>

      val dslCtxt = DSL.using(connection, SQLDialect.POSTGRES)

      queryFn apply dslCtxt

    }
  }

}
