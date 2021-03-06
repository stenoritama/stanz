package com.github.sdual.stanz.prototype.algorithm

import com.github.sdual.stanz.prototype.Probability
import com.github.sdual.stanz.prototype.Stanz._
import com.github.sdual.stanz.prototype.monad.Distribution.Conditional
import com.github.sdual.stanz.prototype.monad.Free.Trampoline
import com.github.sdual.stanz.prototype.monad.{Distribution, Trampoline}

trait PriorDistribution {
  def proposal[A](dist: Distribution[A]): Distribution[(A, Probability)]
}

class PriorDistributionImpl extends PriorDistribution {
  def proposal[A](dist: Distribution[A]): Distribution[(A, Probability)] = {

    def loop(dist: Distribution[A]): Trampoline[Distribution[(A, Probability)]] = {
      dist match {
        case cond: Conditional[A] => Trampoline.more(
          loop(cond.dist).map(l => l.map(vp => (vp._1, vp._2 * cond.likelihood(vp._1))))
        )
        case fm: Distribution.FlatMap[A, _] =>
          Trampoline.more(
            loop(fm.dist).map(l => l.flatMap(vp => fm.f(vp._1).map(y => (y, vp._2))))
          )
        case other => Trampoline.done(other.map(v => (v, 1.0)))
      }
    }

    loop(dist).runT
  }
}

object PriorDistributionImpl {
  def apply(): PriorDistribution = new PriorDistributionImpl()
}
