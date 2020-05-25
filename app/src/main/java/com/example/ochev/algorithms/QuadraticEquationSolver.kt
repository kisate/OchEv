package com.example.ochev.algorithms

import kotlin.math.abs
import kotlin.math.sqrt

class QuadraticEquationSolver {
    companion object {
        fun solveEquation(a: Float, b: Float, c: Float): MutableList<Float> {
            if (abs(a) <= 0.00001) {
                return if (abs(b) <= 0.00001) ArrayList()
                else mutableListOf(-c / b)
            } else {
                val d = b * b - 4 * a * c
                if (d < 0) return ArrayList()
                if (abs(d) < 0.00001) return mutableListOf(-b / (2 * a))
                return mutableListOf(
                    (-b + sqrt(d)) / (2 * a),
                    (-b - sqrt(d)) / (2 * a)
                )
            }
        }
    }
}