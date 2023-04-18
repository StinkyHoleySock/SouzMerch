package com.example.souzmerch.shared.utils

import kotlin.math.roundToInt

object DistanceUtil {
    // Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
    // using the "Inverse Formula" (section 4)
    fun computeDistanceAndBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double,
        results: FloatArray
    ) {
        var lat1 = lat1
        var lon1 = lon1
        var lat2 = lat2
        var lon2 = lon2

        val MAXITERS = 20
        // Convert lat/long to radians
        lat1 *= Math.PI / 180.0
        lat2 *= Math.PI / 180.0
        lon1 *= Math.PI / 180.0
        lon2 *= Math.PI / 180.0

        val a = 6378137.0 // WGS84 major axis
        val b = 6356752.3142 // WGS84 semi-major axis
        val f = (a - b) / a
        val aSqMinusBSqOverBSq = (a * a - b * b) / (b * b)
        val L = lon2 - lon1
        var A = 0.0
        val U1 = Math.atan((1.0 - f) * Math.tan(lat1))
        val U2 = Math.atan((1.0 - f) * Math.tan(lat2))
        val cosU1 = Math.cos(U1)
        val cosU2 = Math.cos(U2)
        val sinU1 = Math.sin(U1)
        val sinU2 = Math.sin(U2)
        val cosU1cosU2 = cosU1 * cosU2
        val sinU1sinU2 = sinU1 * sinU2
        var sigma = 0.0
        var deltaSigma = 0.0
        var cosSqAlpha = 0.0
        var cos2SM = 0.0
        var cosSigma = 0.0
        var sinSigma = 0.0
        var cosLambda = 0.0
        var sinLambda = 0.0
        var lambda = L // initial guess

        for (i in 0 until MAXITERS) {
            val lambdaOrig = lambda

            cosLambda = Math.cos(lambda)
            sinLambda = Math.sin(lambda)

            val t1 = cosU2 * sinLambda
            val t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda
            val sinSqSigma = t1 * t1 + t2 * t2 // (14)

            sinSigma = Math.sqrt(sinSqSigma)
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda // (15)
            sigma = Math.atan2(sinSigma, cosSigma) // (16)

            val sinAlpha = if (sinSigma == 0.0) 0.0 else cosU1cosU2 * sinLambda / sinSigma // (17)

            cosSqAlpha = 1.0 - sinAlpha * sinAlpha
            cos2SM = if (cosSqAlpha == 0.0) 0.0 else cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha // (18)

            val uSquared = cosSqAlpha * aSqMinusBSqOverBSq // defn
            A = 1 + uSquared / 16384.0 * (4096.0 + uSquared * (-768 + uSquared * (320.0 - 175.0 * uSquared))) // (3)
            val B = uSquared / 1024.0 * (256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared))) // (4)
            val C = f / 16.0 * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)) // (10)
            val cos2SMSq = cos2SM * cos2SM

            deltaSigma = (B * sinSigma * (cos2SM + B / 4.0 * (cosSigma * (-1.0 + 2.0 * cos2SMSq) - B / 6.0 * cos2SM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SMSq)))) // (6)
            lambda = L + (1.0 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SM + C * cosSigma * (-1.0 + 2.0 * cos2SM * cos2SM))) // (11)

            val delta = (lambda - lambdaOrig) / lambda

            if (Math.abs(delta) < 1.0e-12) {
                break
            }
        }

        val distance = (b * A * (sigma - deltaSigma)).toFloat()
        results[0] = distance
        if (results.size > 1) {

            var initialBearing = Math.atan2(cosU2 * sinLambda, cosU1 * sinU2 - sinU1 * cosU2 * cosLambda).toFloat()
            initialBearing *= (180.0 / Math.PI).toFloat()
            val azimuth = if (initialBearing < 180 && initialBearing > 0) initialBearing else 360 + initialBearing
            results[1] = ((azimuth * 100.0).roundToInt() / 100.0).toFloat()

            if (results.size > 2) {
                var finalBearing = Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2 + cosU1 * sinU2 * cosLambda).toFloat()
                finalBearing *= (180.0 / Math.PI).toFloat()
                results[2] = finalBearing
            }
        }
    }
}