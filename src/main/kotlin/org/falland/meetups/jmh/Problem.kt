package org.falland.meetups.jmh

class Problem {


    val SIGN_BIT: Int = -0x80000000
    var EXP_BITS: Int = 0x7ff00000
    var tiny: Double = 1.0e-300

    private fun __LO(x: Double): Int {
        val transducer = java.lang.Double.doubleToRawLongBits(x)
        return transducer.toInt()
    }

    private fun __HI(x: Double): Int {
        val transducer = java.lang.Double.doubleToRawLongBits(x)
        return (transducer shr 32).toInt()
    }

    fun __HI_LO(high: Int, low: Int): Double {
        return java.lang.Double.longBitsToDouble(
            (high.toLong() shl 32) or
                    (low.toLong() and 0xffffffffL)
        )
    }

    fun doStuff(list: List<Int>): Double {
        var complexSum = 0.0
        for (element in list) {
            var z = 0.0
            val sign = SIGN_BIT
            /*unsigned*/
            var r: Int
            var t1: Int
            var s1: Int
            var ix1: Int
            var q1: Int
            var ix0: Int
            var s0: Int
            var t: Int
            var i: Int
            val x = element.toDouble()

            ix0 = __HI(x) // high word of x
            ix1 = __LO(x) // low word of x


            // take care of Inf and NaN
            if ((ix0 and EXP_BITS) == EXP_BITS) { // sqrt(NaN)=NaN, sqrt(+inf)=+inf, sqrt(-inf)=sNaN
                complexSum += x * x + x
                break
            }

            // take care of zero
            if (ix0 <= 0) { // sqrt(+-0) = +-0
                if (((ix0 and (sign.inv())) or ix1) == 0) {
                    complexSum += x
                    break
                }
                else if (ix0 < 0) { // sqrt(-ve) = sNaN
                    complexSum += (x - x) / (x - x)
                    break
                }
            }

            // normalize x
            var m = (ix0 shr 20)
            if (m == 0) { // subnormal x
                while (ix0 == 0) {
                    m -= 21
                    ix0 = ix0 or (ix1 ushr 11) // unsigned shift
                    ix1 = ix1 shl 21
                }
                i = 0
                while ((ix0 and 0x00100000) == 0) {
                    ix0 = ix0 shl 1
                    i++
                }
                m -= i - 1
                ix0 = ix0 or (ix1 ushr (32 - i)) // unsigned shift
                ix1 = ix1 shl i
            }
            m -= 1023 // unbias exponent */
            ix0 = (ix0 and 0x000fffff) or 0x00100000
            if ((m and 1) != 0) {        // odd m, double x to make it even
                ix0 += ix0 + ((ix1 and sign) ushr 31) // unsigned shift
                ix1 += ix1
            }
            m = m shr 1 // m = [m/2]


            // generate sqrt(x) bit by bit
            ix0 += ix0 + ((ix1 and sign) ushr 31) // unsigned shift
            ix1 += ix1
            var q = 0.also { s1 = it }.also { s0 = it }.also { q1 = it } // [q,q1] = sqrt(x)
            r = 0x00200000 // r = moving bit from right to left

            while (r != 0) {
                t = s0 + r
                if (t <= ix0) {
                    s0 = t + r
                    ix0 -= t
                    q += r
                }
                ix0 += ix0 + ((ix1 and sign) ushr 31) // unsigned shift
                ix1 += ix1
                r = r ushr 1 // unsigned shift
            }

            r = sign
            while (r != 0) {
                t1 = s1 + r
                t = s0
                if ((t < ix0) ||
                    ((t == ix0) && (Integer.compareUnsigned(t1, ix1) <= 0))
                ) { // t1 <= ix1
                    s1 = t1 + r
                    if (((t1 and sign) == sign) && (s1 and sign) == 0) {
                        s0 += 1
                    }
                    ix0 -= t
                    if (Integer.compareUnsigned(ix1, t1) < 0) {  // ix1 < t1
                        ix0 -= 1
                    }
                    ix1 -= t1
                    q1 += r
                }
                ix0 += ix0 + ((ix1 and sign) ushr 31) // unsigned shift
                ix1 += ix1
                r = r ushr 1 // unsigned shift
            }


            // use floating add to find out rounding direction
            if ((ix0 or ix1) != 0) {
                z = 1.0 - tiny // trigger inexact flag
                if (z >= 1.0) {
                    z = 1.0 + tiny
                    if (q1 == -0x1) {
                        q1 = 0
                        q += 1
                    } else if (z > 1.0) {
                        if (q1 == -0x2) {
                            q += 1
                        }
                        q1 += 2
                    } else {
                        q1 += (q1 and 1)
                    }
                }
            }
            ix0 = (q shr 1) + 0x3fe00000
            ix1 = q1 ushr 1 // unsigned shift
            if ((q and 1) == 1) {
                ix1 = ix1 or sign
            }
            ix0 += (m shl 20)
            complexSum += __HI_LO(ix0, ix1)
        }
        return complexSum;
    }
}





