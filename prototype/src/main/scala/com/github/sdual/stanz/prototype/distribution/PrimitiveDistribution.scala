package com.github.sdual.stanz.prototype.distribution

import com.github.sdual.stanz.prototype.Probability
import com.github.sdual.stanz.prototype.monad.Distribution
import com.github.sdual.stanz.prototype.monad.Distribution.Primitive

import scala.util.Random

trait PrimitiveDistribution[A] {
  def sample(random: Random): A
}

object PrimitiveDistribution {

  def bernoulli(prob: Probability): Distribution[Boolean] = Primitive(Bernoulli(prob))

  def gaussian(mean: Double, stdDev: Double): Distribution[Double] = Primitive(Gaussian(mean, stdDev))

}

class Bernoulli(prob: Probability) extends PrimitiveDistribution[Boolean] {
  def sample(random: Random): Boolean = {
    prob > random.nextDouble()
  }
}

object Bernoulli {
  def apply(prob: Probability): PrimitiveDistribution[Boolean] = new Bernoulli(prob)
}

class Gaussian(mean: Double, stdDev: Double) extends PrimitiveDistribution[Double] {
  def sample(random: Random): Double = {
    val sampled = random.nextGaussian()
    (stdDev * sampled) + mean
  }
}

object Gaussian {
  def apply(mean: Double, stdDev: Double): PrimitiveDistribution[Double] =
    new Gaussian(mean, stdDev)
}
