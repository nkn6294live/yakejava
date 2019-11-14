package home.tut.yake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cue.lang.SentenceIterator;
import cue.lang.WordIterator;

public class Wrapper {

	public static int min(int... values) {
		int min = Integer.MAX_VALUE;
		for (int value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}
	
	public static double min(double... values) {
		double min = Double.MAX_VALUE;
		for (double value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}
	
	public static int max(int... values) {
		int max = Integer.MIN_VALUE;
		for (int value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}
	
	public static double max(double... values) {
		double max = Double.MIN_VALUE;
		for (double value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}
	
	public static double max(Collection<Double> values) {
		double max = Double.MIN_VALUE;
		for (double value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}
	
	public static Double sum(List<Double> numbers) {
		Double sum = 0.0;
		for (Double number : numbers) {
			sum += number;
		}
		return sum;
	}
	public static Double sum(Double... numbers) {
		Double sum = 0.0;
		for (Double number : numbers) {
			sum += number;
		}
		return sum;
	}

	
	public static class jellyfish {

//		public static double jaro_winkler(String left, String right) {
//			return new JaroWinklerDistance().apply(left, right);
//		}
		
//		public static int levenshtein_distance(String seq1, String seq2) {
//			return new LevenshteinDistance().apply(seq1, seq2);
//		}
		
		public static double jaro_winkler(final CharSequence left, final CharSequence right) {
			if (left == null || right == null) {
	            throw new IllegalArgumentException("CharSequences must not be null");
	        }
			final double defaultScalingFactor = 0.1;
	        final int[] mtp = jaro_matches(left, right);
	        final double m = mtp[0];
	        if (m == 0) {
	            return 0D;
	        }
	        final double j = ((m / left.length() + m / right.length() + (m - (double) mtp[1] / 2) / m)) / 3;
	        final double jw = j < 0.7D ? j : j + defaultScalingFactor * mtp[2] * (1D - j);
	        return jw;
		}
		
		public static int levenshtein_distance(final CharSequence left, final CharSequence right, final Integer threshold) {
			if (threshold == null) {
	            return levenshtein_unlimitedCompare(left, right);
	        }
			return levenshtein_limitedCompare(left, right, threshold);
		}
		
		public static int levenshtein_distance(final CharSequence left, final CharSequence right) {
			return levenshtein_distance(left, right, null);
		}
		
		private static int[] jaro_matches(final CharSequence first, final CharSequence second) {
	        CharSequence max, min;
	        if (first.length() > second.length()) {
	            max = first;
	            min = second;
	        } else {
	            max = second;
	            min = first;
	        }
	        final int range = Math.max(max.length() / 2 - 1, 0);
	        final int[] matchIndexes = new int[min.length()];
	        Arrays.fill(matchIndexes, -1);
	        final boolean[] matchFlags = new boolean[max.length()];
	        int matches = 0;
	        for (int mi = 0; mi < min.length(); mi++) {
	            final char c1 = min.charAt(mi);
	            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
	                if (!matchFlags[xi] && c1 == max.charAt(xi)) {
	                    matchIndexes[mi] = xi;
	                    matchFlags[xi] = true;
	                    matches++;
	                    break;
	                }
	            }
	        }
	        final char[] ms1 = new char[matches];
	        final char[] ms2 = new char[matches];
	        for (int i = 0, si = 0; i < min.length(); i++) {
	            if (matchIndexes[i] != -1) {
	                ms1[si] = min.charAt(i);
	                si++;
	            }
	        }
	        for (int i = 0, si = 0; i < max.length(); i++) {
	            if (matchFlags[i]) {
	                ms2[si] = max.charAt(i);
	                si++;
	            }
	        }
	        int halfTranspositions = 0;
	        for (int mi = 0; mi < ms1.length; mi++) {
	            if (ms1[mi] != ms2[mi]) {
	                halfTranspositions++;
	            }
	        }
	        int prefix = 0;
	        for (int mi = 0; mi < Math.min(4, min.length()); mi++) {
	            if (first.charAt(mi) == second.charAt(mi)) {
	                prefix++;
	            } else {
	                break;
	            }
	        }
	        return new int[] {matches, halfTranspositions, prefix};
	    }
	
		private static int levenshtein_limitedCompare(CharSequence left, CharSequence right, final int threshold) { // NOPMD
		        if (left == null || right == null) {
		            throw new IllegalArgumentException("CharSequences must not be null");
		        }
		        if (threshold < 0) {
		            throw new IllegalArgumentException("Threshold must not be negative");
		        }

		        int n = left.length(); 
		        int m = right.length();
		        if (n == 0) {
		            return m <= threshold ? m : -1;
		        } else if (m == 0) {
		            return n <= threshold ? n : -1;
		        }

		        if (n > m) {
		            final CharSequence tmp = left;
		            left = right;
		            right = tmp;
		            n = m;
		            m = right.length();
		        }
		        if (m - n > threshold) {
		            return -1;
		        }

		        int[] p = new int[n + 1]; 
		        int[] d = new int[n + 1]; 
		        int[] tempD; 

		        final int boundary = Math.min(n, threshold) + 1;
		        for (int i = 0; i < boundary; i++) {
		            p[i] = i;
		        }
		        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
		        Arrays.fill(d, Integer.MAX_VALUE);

		        for (int j = 1; j <= m; j++) {
		            final char rightJ = right.charAt(j - 1); 
		            d[0] = j;
		            final int min = Math.max(1, j - threshold);
		            final int max = j > Integer.MAX_VALUE - threshold ? n : Math.min(
		                    n, j + threshold);
		            if (min > 1) {
		                d[min - 1] = Integer.MAX_VALUE;
		            }

		            for (int i = min; i <= max; i++) {
		                if (left.charAt(i - 1) == rightJ) {
		                    d[i] = p[i - 1];
		                } else {
		                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
		                }
		            }

		            tempD = p;
		            p = d;
		            d = tempD;
		        }
		        if (p[n] <= threshold) {
		            return p[n];
		        }
		        return -1;
		    }
		
		private static int levenshtein_unlimitedCompare(CharSequence left, CharSequence right) {
	        if (left == null || right == null) {
	            throw new IllegalArgumentException("CharSequences must not be null");
	        }
	        int n = left.length();
	        int m = right.length();

	        if (n == 0) {
	            return m;
	        } else if (m == 0) {
	            return n;
	        }

	        if (n > m) {
	            final CharSequence tmp = left;
	            left = right;
	            right = tmp;
	            n = m;
	            m = right.length();
	        }

	        final int[] p = new int[n + 1];
	        int i, j, upperLeft, upper;

	        char rightJ;
	        int cost;

	        for (i = 0; i <= n; i++) {
	            p[i] = i;
	        }

	        for (j = 1; j <= m; j++) {
	            upperLeft = p[0];
	            rightJ = right.charAt(j - 1);
	            p[0] = j;

	            for (i = 1; i <= n; i++) {
	                upper = p[i];
	                cost = left.charAt(i - 1) == rightJ ? 0 : 1;
	                p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upperLeft + cost);
	                upperLeft = upper;
	            }
	        }

	        return p[n];
	    }
	}
	
	public static class Levenshtein {
		
		public static double ratio(final CharSequence seq1, final CharSequence seq2) {
			double str_distance = Levenshtein.distance(seq1, seq2);
			int str_length = Math.max(seq1.length(), seq2.length());
			return Levenshtein.__ratio(str_distance, str_length);
		}
		
		public static int distance(final CharSequence seq1, final CharSequence seq2) {
			int size_x = seq1.length() + 1;
			int size_y = seq2.length() + 1;
			int[][] matrix = new int[size_x][size_y];//np.zeros(size_x, size_y);
//			TODO update all element -> 0
//			for (int x = 0; x < size_x; x++) {
//				for (int y = 0; y < size_y; y++) {
//					matrix[x][y] = 0;
//				}
//			}
			for (int x = 0; x < size_x; x++) {
				matrix[x][0] = x;
			}
			for (int y = 0; y < size_y; y++) {
				matrix[0][y] = y;
			}
			for (int x = 1; x < size_x; x++) {
				for (int y = 1; y < size_y; y++) {
					if (seq1.charAt(x - 1) == seq2.charAt(y - 1)) {
						matrix[x][y] = min(
								matrix[x - 1][y] + 1,
								matrix[x - 1][y - 1],
								matrix[x][y - 1] + 1
								);
					} else {
						matrix[x][y] = min(
								matrix[x - 1][y] + 1,
								matrix[x - 1][y - 1] + 1,
								matrix[x][y - 1] + 1
								);
					}
				}
			}
			return matrix[size_x - 1][size_y - 1];
		}
	
		private static double __ratio(double distance, int str_length) {
			return 1 - distance / str_length;
		}
	}

	// TODO NEED UPDATE
	
	
	// TODO NEED IMPLEMENT
	
	public static class Tuple<T> implements Iterable<T> {
		
		public static <T> Tuple<T> array(T[] values) {
			return new Tuple<T>(values);
		}
		
		@SafeVarargs
		public static <T> Tuple<T> from(T... values) {
			return new Tuple<T>(values);
		}
		
		public static <T> Tuple<T> empty() {
			return Tuple.from();
		}
		
		public int length() {
			return this.values.length;
		}
		
		public T[] values() {
			return this.values;
		}
		
		public T value(int index) {
			if (index < 0 || index > this.values.length) {
				throw new IndexOutOfBoundsException();
			}
			return this.values[index];
		}
		
		@Override
		public Iterator<T> iterator() {
			return Arrays.stream(this.values).iterator();
		}
		
		private T[] values;
		
		private Tuple(T[] values) {
			if (values == null) {
				throw new NullPointerException();
			}
			this.values = values;
		}
		
	}
//	import numpy as np
	public static class np {
		public static double median(Collection<? extends Number> list) {
			List<Number> sorted = list.stream().sorted().collect(Collectors.toList());
			double median;
			int size = sorted.size();
			if (size % 2 == 0) {
				median = (sorted.get(size / 2).doubleValue() + sorted.get(size / 2 - 1).doubleValue()) / 2;
			} else {
				median = sorted.get(size / 2).doubleValue();
			}
			return median;
		}
		public static double[][] zeros(int size_x, int size_y) {
			double[][] matrix = new double[size_x][size_y];
//			TODO update all element -> 0
//			for (int x = 0; x < size_x; x++) {
//				for (int y = 0; y < size_y; y++) {
//					matrix[x][y] = 0;
//				}
//			}
			return matrix;
		}
		public static double mean(Collection<? extends Number> list) {
			double sum = 0;
			for (Number item : list) {
				sum+= item.doubleValue();
			}
			return sum / list.size();
		}
		public static double prod(Collection<? extends Number> list) {
			double result = 1.0;
			for (Number n : list) {
				double d = n.doubleValue();
				if (d == 0) return 0;
				result *= d;
			}
			return result;
		}
		public static np array(List<? extends Number> objects) {
			return new np(objects);
		}
		public static np array(Number... objects) {
			return new np(Arrays.stream(objects).collect(Collectors.toList()));
		}
		
		public Double mean() {
			return np.mean(this.values);
		}
		
		public double std() {
//			sqrt(mean(abs(x - x.mean())**2)).
			double mean = this.mean();
			double sum = 0.0;
			for (Number item : this.values) {
				 sum += Math.pow(item.doubleValue() - mean, 2);
			}
			return Math.sqrt(sum / this.values.size());
		}
		
		protected List<? extends Number> values;
		
		protected np(List<? extends Number> values) {
			this.values = values;
		}
	}
	
	public static class nx_edge {
		
		public nx_edge(nx_node u, nx_node v, Map<String, Object> d) {
			this.u = u;
			this.v = v;
			this.d = d;
		}
		
		public nx_node d(String name) {
			return this.d(name);
		}
		
		public Set<String> dKeys() {
			return this.d.keySet();
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((u == null) ? 0 : u.hashCode());
			result = prime * result + ((v == null) ? 0 : v.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			nx_edge other = (nx_edge) obj;
			if (u == null) {
				if (other.u != null) { 
					return false;
				}
			} else if (!u.equals(other.u)) {
				return false;
			}
			if (v == null) { 
				if (other.v != null) {
					return false;
				}
			} else if (!v.equals(other.v)) {				
				return false;
			}
			return true;
		}

		public nx_node u;
		public nx_node v;
		public Map<String, Object> d;
		
		public Tuple<Object> tuple() {
			return Tuple.from(u, v, d);
		}
	}
	
	public static class nx_node {
		
		public nx_node(int id) {
			this.id = id;
			this.d = new HashMap<>();
		}
		
		public int id() {
			return this.id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			nx_node other = (nx_node) obj;
			if (id != other.id) return false;
			return true;
		}

		public nx_node d(String name) {
			return this.d(name);
		}
		
		public Set<String> dKeys() {
			return this.d.keySet();
		}

		private int id;
		private Map<String, Object> d;
	}
	
	public static class nx {
		public List<nx_edge> out_edges(int node_id) {
			return this.edges.stream()
					.filter(item -> item.u.id == node_id)
					.collect(Collectors.toList());
		}
		public List<nx_edge> out_edges(nx_node node) {
			return this.edges.stream()
					.filter(item -> item.u.equals(node))
					.collect(Collectors.toList());
		}
		
		public List<nx_edge> in_edges(int node_id) {
			return this.edges.stream()
					.filter(item -> item.v.id == node_id)
					.collect(Collectors.toList());
		}
		public List<nx_edge> in_edges(nx_node node) {
			return this.edges.stream()
					.filter(item -> item.v.equals(node))
					.collect(Collectors.toList());
		}
		
		public nx_edge add_edge(nx_node u, nx_node v, Map<String, Object> d) {
			nx_edge edge = this.get(u, v);
			if (edge == null) {
				edge = new nx_edge(this.add_node(u), this.add_node(v), d);
				this.edges.add(edge);
			}
			return edge;
		}
		
		public nx_edge add_edge(int u, int v, Map<String, Object> d) {
			nx_edge edge = this.get(u, v);
			if (edge == null) {				
				edge = new nx_edge(this.add_node(u), this.add_node(v), d);
				this.edges.add(edge);
			}
			return edge;
		}
		
		public nx_node add_node(nx_node u) {
			nx_node old_node = this.nodes.put(u.id, u);
			if (old_node != null) {
				for (nx_edge edge : this.edges) {
					if (edge.u.id == u.id) {
						edge.u = u;
					}
					if (edge.v.id == u.id) {
						edge.v = u;
					} 
				}
			}
			return u;
		}
		
		public nx_node add_node(int node_id) {
			return add_node(new nx_node(node_id));
		}
		
		public nx_edge get(int u, int v) {
			return this.edges.stream()
					.filter(item -> item.u.id == u)
					.filter(item-> item.v.id == v)
					.findAny().orElse(null);
		}
		public nx_edge get(nx_node u, nx_node v) {
			return this.edges.stream()
					.filter(item -> item.u.equals(u))
					.filter(item-> item.v.equals(v))
					.findAny().orElse(null);
		}
		public boolean check_contain(int u, int v) {
			return false;
		}
		public double prob(int u, int v, String name) {
			return (double)this.get(u, v).d.get(name);
		}
		public double prob(int u, int v, String name, double value) {
			return (double)get(u, v).d.put(name, value);
		}
		public boolean has_edge(int u, int v) {
			return this.get(u, v) != null;
		}
		
		public static nx DiGraph() {
			return new nx();
		}
		
		public Object d(String name) {
			return this.d.get(name);
		}
		
		private List<nx_edge> edges;
		private Map<Integer, nx_node> nodes;
		private Map<String, Object> d;
		
		private nx() {
			this.edges = new ArrayList<>();
			this.nodes = new HashMap<>();
			this.d = new HashMap<>(); 
		}
		
	}
	
	public static class segtok {
//		from segtok.segmenter import split_multi
		public static List<String> split_multi(String text) {
			List<String> results = new ArrayList<>();
			SentenceIterator sentenceIterator = new SentenceIterator(text, Locale.ENGLISH);
			for (final String word : sentenceIterator) {
				results.add(word);
			}
			return results;
		}
//		from segtok.tokenizer import web_tokenizer
		public static List<String> web_tokenizer(String sentence) {
			List<String> results = new ArrayList<>();
			WordIterator wordIterator = new WordIterator(sentence);
			for (final String word : wordIterator) {
			    results.add(word);
			}
			return results;
		}
//		from segtok.tokenizer import split_contractions
		public static List<String> split_contractions(List<String> tokens) {
			// TODO update split_contractions
			return tokens;
		}
	}
	
	}
