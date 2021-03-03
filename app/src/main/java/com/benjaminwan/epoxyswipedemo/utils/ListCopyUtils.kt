package com.benjaminwan.epoxyswipedemo.utils

/**
 * 复制List并添加元素[value]，返回处理后的List
 * @param  value
 * @return List
 */
fun <T> List<T>.copyAdd(value: T): List<T> = toMutableList().apply { add(value) }

/**
 * 复制List并添加集合[values]，返回处理后的List
 * @param  values
 * @return List
 */
fun <T> List<T>.copyAddAll(values: Collection<T>): List<T> =
    toMutableList().apply { addAll(values) }

/**
 * 复制List并添加集合[values]，返回处理后的List
 * @param  values
 * @return List
 */
fun <T> List<T>.copyAddAll(values: Iterable<T>): List<T> = toMutableList().apply { addAll(values) }

/**
 * 复制List并添加集合[values]，返回处理后的List
 * @param  values
 * @return List
 */
fun <T> List<T>.copyAddAll(values: Sequence<T>): List<T> = toMutableList().apply { addAll(values) }

/**
 * 复制List并添加集合[values]，返回处理后的List
 * @param  values
 * @return List
 */
fun <T> List<T>.copyAddAll(values: Array<out T>): List<T> = toMutableList().apply { addAll(values) }

/**
 * 复制List并删除[index]处的元素，返回处理后的List
 * @param  index
 * @return List
 */
fun <T> List<T>.copyRemoveAt(index: Int): List<T> = toMutableList().apply { removeAt(index) }

/**
 * 复制List并删除[value]元素，返回处理后的List
 * @param  value
 * @return List
 */
fun <T> List<T>.copyRemove(value: T): List<T> = toMutableList().apply { remove(value) }

/**
 * 复制List并以[element]替换[index]处的元素，返回处理后的List
 * @param  index
 * @param  element
 * @return List
 */
fun <T> List<T>.copyAndSet(index: Int, element: T): List<T> =
    toMutableList().apply { set(index, element) }

/**
 * 复制List并把[index1]与[index2]处的元素交换位置，返回处理后的List
 * @param  index1
 * @param  index2
 * @return List
 */
fun <T> List<T>.copySwap(index1: Int, index2: Int): List<T> =
    toMutableList().apply { this[index1] = this.set(index2, this[index1]) }