package home.tut.yake;


import static home.tut.yake.Wrapper.max;
import static home.tut.yake.Wrapper.sum;
import static home.tut.yake.Wrapper.segtok.split_contractions;
import static home.tut.yake.Wrapper.segtok.split_multi;
import static home.tut.yake.Wrapper.segtok.web_tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import home.tut.yake.Wrapper.Tuple;
import home.tut.yake.Wrapper.jellyfish;
import home.tut.yake.Wrapper.np;
import home.tut.yake.Wrapper.nx;
import home.tut.yake.Wrapper.nx_edge;

public class DataRepresentation {
	public static final String STOPWORD_WEIGHT = "bi";
	public static final char[] PUNCTUATION = "!\"#$%&\\'()*+,-./:;<=>?@[\\]^_`{|}~".toCharArray();
	
	public static class Term {
		
		public void updateH(String[] features, boolean isVirtual) {
		}
		
		public void updateH(double maxTF, double avgTF, double stdTF, int number_of_sentences, String[] features) {
		}
		public void addOccur(String tag, int sent_id, int pos_sent, int pos_text) {
		}
		
		protected boolean stopword;
		protected int t;
		protected double tf;
		protected nx G;
		protected int id;
		protected double H;
		
		protected Term() {
			this.updateStopword();
		}
		
		protected Double getattr(String feature_name) {
			if ("tf".contentEquals(feature_name)) {
				return this.tf;
			}
			if ("t".contentEquals(feature_name)) {
				return (double)this.t;
			}
			if ("H".contentEquals(feature_name)) {
				return this.H;
			}
			if ("id".contentEquals(feature_name)) {
				return (double)this.id;
			}
//			Object o = null;
//			Field field = null;
//			try {
//				field = this.getClass().getField(feature_name);
//				o = field.get(this);
//			} catch (Exception e) {
//				try {
//					field.setAccessible(true);;
//					o = field.get(this);
//					field.setAccessible(false);
//				} catch (Exception ex) {}
//			}
//			if (o instanceof Number) {						
//				return ((Number)o).doubleValue();
//			}
			return null;
		}
		
		protected void updateStopword() {
			// TODO check stopword
		}
	}
		
	public static class SingleWord extends Term {
		public SingleWord(String unique, int idx, nx graph) {
			super();
			this.unique_term = unique;
	        this.id = idx;
	        this.tf = 0.0f;
	        this.WFreq = 0.0f;
	        this.WCase = 0.0f;
	        this.tf_a = 0.0f;
	        this.tf_n = 0.0f;
	        this.WRel = 1.0f;
	        this.PL = 0.f;
	        this.PR = 0.f;
	        this.occurs = new HashMap<>();
	        this.WPos = 1.0f;
	        this.WSpread = 0.0f;
	        this.H = 0.0f;
	        this.stopword = false;
	        this.G = graph;
	        this.pagerank = 1.0f;
		}
		
	    public int WDR() {
	    	return this.G.out_edges(this.id).size();
	    }
	    
		public double WIR() {
			//return sum( [ d['TF'] for (u,v,d) in self.G.out_edges(self.id, data=True) ] );
	    	double sum = 0;
	    	for (nx_edge edge : this.G.out_edges(this.id)) {
	    		sum += (double)edge.d.get("TF");
	    	}
	    	return sum; 
	    }

	    public double PWR() {
	    	double wir = this.WIR();
	        if(wir == 0) {
	        	return 0;	        	
	        }
	        return this.WDR() / wir;
	    }
	         
	    public int WDL() {	    	
	    	return this.G.in_edges(this.id).size();
	    }

		public double WIL() {
			//return sum( [ d['TF'] for (u,v,d) in self.G.in_edges(self.id, data=True) ] )
	    	double sum = 0;
	    	for (nx_edge edge : this.G.in_edges(this.id)) {
	    		sum += (double)edge.d.get("TF");
	    	}
	    	return sum;
	    }
	    
	    public double PWL() {
	    	double wil = this.WIL();
	    	if (wil == 0) {
	    		return 0;	    		
	    	}
	    	return this.WDL() / wil; 
	    }
	    
	    @Override
	    public void updateH(double maxTF, double avgTF, double stdTF, int number_of_sentences, String[] features) {
	    	boolean isNone 		= features == null || features.length == 0;
	    	boolean isWRel 		= false;
	    	boolean isWFreq 	= false;
	    	boolean isWSpread 	= false;
	    	boolean isWPos 		= false;
	    	boolean isWCase 	= false;
	    	if (features != null) {	    		
	    		for (String feature : features) {
	    			if ("WRel".contentEquals(feature)) {
	    				isWRel = true;
	    			} else if ("WFreq".contentEquals(feature)) {
	    				isWFreq = true;
	    			} else if ("WSpread".contentEquals(feature)) {
	    				isWSpread = true;
	    			} else if ("isWCase".contentEquals(feature)) {
	    				isWCase = true;
	    			} else if ("WPos".contentEquals(feature)) {
	    				isWPos = true;
	    			}
	    		}
	    	}
//	        if (isNone || isWRel) {
//	            this.PL = this.WDL() / maxTF;
//	            this.PR = this.WDR() / maxTF;
//	            this.WRel = ( (0.5 + (this.PWL() * (this.tf / maxTF) + this.PL)) + (0.5 + (this.PWR() * (this.tf / maxTF) + this.PR)) );
//	        }
	        
	        if(isNone || isWRel) {
	        	this.PL = this.WDL() / maxTF;
    			this.PR = this.WDR() / maxTF;
				this.WRel = ((0.5 + (this.PWL() * (this.tf / maxTF))) + (0.5 + (this.PWR() * (this.tf / maxTF))));
	        }
	        if(isNone || isWFreq) {
	        	this.WFreq = this.tf / (avgTF + stdTF);
	        }
	        if(isNone || isWSpread) {
	        	this.WSpread = this.occurs.size() / number_of_sentences;
	        }
	        if (isNone || isWCase) {
	        	this.WCase = Math.max(this.tf_a, this.tf_n) / (1. + Math.log(this.tf));
	        }
	        if(isNone || isWPos) {
	        	this.WPos = Math.log(Math.log(3. + np.median(this.occurs.keySet())));
	        }

			this.H = (this.WPos * this.WRel) / (this.WCase + (this.WFreq / this.WRel) + (this.WSpread / this.WRel));
	    }
	    
	    public void addOccur(String tag, int sent_id, int pos_sent, int pos_text) {
	    	if (!this.occurs.containsKey(sent_id)) {
	    		this.occurs.put(sent_id, new ArrayList<>());
	    	}
	    	this.occurs.get(sent_id).add(Tuple.from(pos_sent, pos_text));
	        this.tf += 1.0;

	        if("a".contentEquals(tag)) {
	        	this.tf_a += 1.0;	        	
	        }
	        if("n".contentEquals(tag)) {
	            this.tf_n += 1.0;
	        }
	    }
	    
	    protected String unique_term;
	    protected double WFreq;
	    protected double WCase;
	    protected double tf_a;
	    protected double tf_n;
	    protected double WRel;
	    protected double PL;
	    protected double PR;
	    protected double WPos;
	    protected double WSpread;
	    protected double pagerank;
	    protected boolean stopword;
	    protected Map<Integer, List<Tuple<?>>> occurs;
	    
		@Override
		protected Double getattr(String feature_name) {
			Double value = super.getattr(feature_name);
			if (value != null) {
				return value;
			}
			
			if ("WFreq".contentEquals(feature_name)) {
				return this.WFreq;
			}
			if ("WCase".contentEquals(feature_name)) {
				return this.WCase;
			}
			if ("tf_a".contentEquals(feature_name)) {
				return this.tf_a;
			}
			if ("tf_n".contentEquals(feature_name)) {
				return this.tf_n;
			}
			if ("WRel".contentEquals(feature_name)) {
				return this.WRel;
			}
			if ("PL".contentEquals(feature_name)) {
				return this.PL;
			}
			if ("PR".contentEquals(feature_name)) {
				return this.PR;
			}
			if ("WPos".contentEquals(feature_name)) {
				return this.WPos;
			}
			if ("WSpread".contentEquals(feature_name)) {
				return this.WSpread;
			}
			if ("pagerank".contentEquals(feature_name)) {
				return this.pagerank;
			}
			// properties
			if ("WDR".contentEquals(feature_name)) {
				return (double)this.WDR();
			}
			if ("WIR".contentEquals(feature_name)) {
				return this.WIR();
			}
			if ("PWR".contentEquals(feature_name)) {
				return this.PWR();
			}
			if ("WDL".contentEquals(feature_name)) {
				return (double)this.WDL();
			}
			if ("WIL".contentEquals(feature_name)) {
				return this.WIL();
			}
			if ("PWL".contentEquals(feature_name)) {
				return this.PWL();
			}
			return null;
		}
	}
	
	public static class ComposedWord extends Term {
	    public ComposedWord(List<Tuple<Object>>terms) {// # [ (tag, word, term_obj) ]
	    		super();
	            if (terms == null) {
	                 this.start_or_end_stopwords = true;
	                 this.tags = new HashSet<>();
	                 return;
	            }
	            List<String> tagBuilder = new ArrayList<>();
	            List<String> unique_kwBuilder = new ArrayList<>();
	            this.terms = new ArrayList<>();
	            for (Tuple<Object> term : terms) {
	            	String tag = (String)term.value(0);
	            	String word = (String)term.value(1);
	            	Term term_obj = (Term)term.value(2);
	            	
	            	tagBuilder.add(tag);
	            	unique_kwBuilder.add(word.toLowerCase());
	            	if (term_obj != null) {	            		
	            		this.terms.add(term_obj);//this.terms = [ w[2] for w in terms if w[2] != None ]
	            	}
	            }
	            this.tags = new HashSet<>();
	            this.tags.add(String.join("",tagBuilder));//this.tags = set([''.join([ w[0] for w in terms ])])
	            this.unique_kw = String.join(" ", unique_kwBuilder);//this.unique_kw = ' '.join( [ w[1].lower() for w in terms ] )
	            this.size = terms.size();
	            this.tf = 0.0;
	            this.integrity = 1.0;
	            this.H = 1.0;
	            this.start_or_end_stopwords = this.terms.get(0).stopword 
	            		|| this.terms.get(this.terms.size() - 1).stopword;//self.start_or_end_stopwords = self.terms[0].stopword or self.terms[-1].stopword
	    }
	    
	    public void uptadeCand(ComposedWord cand) {//(cand) : 
	    	for (String tag : cand.tags) {// for tag in cand.tags: this.tags.add( tag )
	    		this.tags.add(tag);
	    	}
	    }
	       
	    public boolean isValid() {
	    	boolean isValid = false;
	        for (String tag : this.tags) {
	            isValid = isValid || (!tag.contains("u") && !tag.contains("d"));//isValid or ( "u" not in tag and "d" not in tag )
	        }
	        return isValid && !this.start_or_end_stopwords;
	    }
	    
	    public Tuple<Double> get_composed_feature(String feature_name, boolean discart_stopword) {//=True):
//	    	list_of_features = [ getattr(term, feature_name) for term in self.terms if ( discart_stopword and not term.stopword ) or not discart_stopword ]
	    	List<Double> list_of_features = this.terms.stream()
	    		.filter(term -> (discart_stopword && !term.stopword) || !discart_stopword)
	    		.map(term -> term.getattr(feature_name))
	    		.filter(item -> item != null)
	    		.collect(Collectors.toList());
	        double sum_f  = sum(list_of_features);
	        double prod_f = np.prod(list_of_features);
	        return Tuple.from( sum_f, prod_f, prod_f /(sum_f + 1));
	    }
	    
	    public Tuple<Double> get_composed_feature(String feature_name) {
	    	return this.get_composed_feature(feature_name, true);
	    }
	    
	    public Tuple<?> build_features(Integer doc_id, String[] keys, boolean rel, boolean rel_approx, boolean isVirtual, 
	    		String[] features, boolean[] _stopword) {// def build_features(self, doc_id=None, keys=None, rel=True, rel_approx=True, isVirtual=False, features=['WFreq', 'WRel', 'tf', 'WCase', 'WPos', 'WSpread'], _stopword=[True, False]):
	        List<String> columns = new ArrayList<>();
	        Set<String> seen = new HashSet<>();
	        List<Object> features_cand = new ArrayList<>();

	        if(doc_id != null) {
	            columns.add("doc_id");
	            features_cand.add(doc_id);
	        }
	        if(keys != null) {
	            if(rel) {
	                columns.add("rel");
	                boolean isContain = this.unique_kw == null ? false : 
	                	Arrays.stream(keys).filter(key -> this.unique_kw.contentEquals(key)).findFirst().orElse(null) != null;
	                if(isContain || isVirtual) {
	                    features_cand.add(1);
	                    seen.add(this.unique_kw);
	                } else {
	                    features_cand.add(0);
	                }
	            }
	            if(rel_approx) {
	                columns.add("rel_approx");
	                Tuple<?> max_gold_ = Tuple.from("", 0.);
	                for (String gold_key : keys) {
	                    double dist = 1.0 - jellyfish.levenshtein_distance(gold_key, this.unique_kw) / Math.max(gold_key.length(), 
	                    		this.unique_kw.length()); 
	                    if((double)max_gold_.value(1) < dist) {
	                        max_gold_ = Tuple.from( gold_key, dist );
	                    }
	                }
	                features_cand.add((double)max_gold_.value(1));
	            }
	        }

	        columns.add("kw");
	        features_cand.add(this.unique_kw);
	        columns.add("h");
	        features_cand.add(this.H);
	        columns.add("tf");
	        features_cand.add(this.tf);
	        columns.add("size");
	        features_cand.add(this.size);
	        columns.add("isVirtual");
	        features_cand.add(isVirtual ? 1 : 0);//int(isVirtual);

	        for(String feature_name : features) {

	            for (boolean discart_stopword : _stopword) {
	                Tuple<Double> composed_feature = this.get_composed_feature(feature_name, discart_stopword);
	                double f_sum = composed_feature.value(0);
	                double f_prod = composed_feature.value(1);
	                double f_sum_prod = composed_feature.value(2);
	                columns.add(String.format("%ss_sum_K%s", discart_stopword ? "n" : "", feature_name));
	                features_cand.add(f_sum);

	                columns.add(String.format("%ss_prod_K%s", discart_stopword ? "n" : "", feature_name));
	                features_cand.add(f_prod);

	                columns.add(String.format("%ss_sum_prod_K%s", discart_stopword ? "n" : "", feature_name));
	                features_cand.add(f_sum_prod);
	            }
	        }
	        return Tuple.from(features_cand, columns, seen);
	    }
	    public Tuple<?> build_features() {
	    	return this.build_features(null, new String[] {}, true, true, false, new String[] {"WFreq", "WRel", "tf", "WCase", "WPos", "WSpread"}, new boolean[] {true, false});
	    }
	    public Tuple<?> build_features(Integer doc_id) {
	    	return this.build_features(doc_id, new String[] {}, true, true, false, new String[] {"WFreq", "WRel", "tf", "WCase", "WPos", "WSpread"}, new boolean[] {true, false});
	    }
	    public Tuple<?> build_features(Integer doc_id, String[] keys) {
	    	return this.build_features(doc_id, keys, true, true, false, new String[] {"WFreq", "WRel", "tf", "WCase", "WPos", "WSpread"}, new boolean[] {true, false});
	    }
	    public Tuple<?> build_features(Integer doc_id, String[] keys, boolean rel) {
	    	return this.build_features(doc_id, keys, rel, true, false, new String[] {"WFreq", "WRel", "tf", "WCase", "WPos", "WSpread"}, new boolean[] {true, false});
	    }
	    public Tuple<?> build_features(Integer doc_id, String[] keys, boolean rel, boolean rel_approx) {
	    	return this.build_features(doc_id, keys, rel, rel_approx, false, new String[] {"WFreq", "WRel", "tf", "WCase", "WPos", "WSpread"}, new boolean[] {true, false});
	    }
	    public Tuple<?> build_features(Integer doc_id, String[] keys, boolean rel, boolean rel_approx, boolean isVirtual) {
	    	return this.build_features(doc_id, keys, rel, rel_approx, isVirtual, new String[] {"WFreq", "WRel", "tf", "WCase", "WPos", "WSpread"}, new boolean[] {true, false});
	    }
	    public Tuple<?> build_features(Integer doc_id, String[] keys, boolean rel, boolean rel_approx, boolean isVirtual, String[] features) {
	    	return this.build_features(doc_id, keys, rel, rel_approx, isVirtual, features, new boolean[] {true, false});
	    }
	    
	    public void updateH(String[] features, boolean isVirtual) {//(self, features=None, isVirtual=False):
	    	boolean isKPF = false;
	    	boolean isNone = features == null || features.length == 0 ? true : false;
	    	if (features != null) {
	    		for (String feature : features) {
	    			if ("KPF".contentEquals(feature)) {
	    				isKPF = true;
	    			}
	    		}
	    	}
	    	double sum_H  = 0.0;
	        double prod_H = 1.0;
	        for (int t = 0; t < this.terms.size(); t++) {
	        	Term term_base = this.terms.get(t);
	            if(!term_base.stopword) {
	                sum_H += term_base.H;
	                prod_H *= term_base.H;
	            } else {
	                if("bi".contentEquals(STOPWORD_WEIGHT)) {
	                    double prob_t1 = 0.0;
	                    if((t - 1) >= 0 && term_base.G.has_edge(this.terms.get(t-1).id, this.terms.get(t).id)) {
	                        prob_t1 = term_base.G.prob(this.terms.get(t-1).id, this.terms.get(t).id, "TF") / this.terms.get(t-1).tf;
	                    }
	                    double prob_t2 = 0.0;
	                    if(term_base.G.has_edge(this.terms.get(t).id, this.terms.get(t+1).id)) {
	                        prob_t2 = term_base.G.prob(this.terms.get(t).id, this.terms.get(t+1).id, "TF") / this.terms.get(t+1).tf;
	                    }
	                    double prob = prob_t1 * prob_t2;
	                    prod_H *= (1 + (1 - prob ) );
	                    sum_H -= (1 - prob);
	                } else if ("h".contentEquals(STOPWORD_WEIGHT)) {
	                    sum_H += term_base.H;
	                    prod_H *= term_base.H;
	                } else if ("none".contentEquals(STOPWORD_WEIGHT)) {
	                }
	            }
	        }
	        double tf_used = 1.0;
	        if(isNone || isKPF) {
	            tf_used = this.tf;
	        }
	        if(isVirtual) {
    			tf_used = np.mean(terms.stream().map(term_obj -> term_obj.tf).collect(Collectors.toList()));//np.mean( [term_obj.tf for term_obj in self.terms] )
	        }
	        this.H = prod_H / ( ( sum_H + 1 ) * tf_used );
		}
	    
	    public void updateH(String[] features) {
	    	this.updateH(features, false);
	    }
	    
	    public void updateH() {
	    	this.updateH(null);
	    }
	    
	    public void updateH_old(String[] features, boolean isVirtual) {//def updateH_old(self, features=None, isVirtual=False):
	    	boolean isKPF = false;
	    	boolean isNone = features == null || features.length == 0 ? true : false;
	    	if (features != null) {
	    		for (String feature : features) {
	    			if ("KPF".contentEquals(feature)) {
	    				isKPF = true;
	    			}
	    		}
	    	}
	        double sum_H  = 0.0;
	        double prod_H = 1.0;
	        
	        for (int t = 0; t < this.terms.size(); t++) {
	        	Term term_base = this.terms.get(t);
	            if(isVirtual && term_base.tf==0) {
	                continue;
	            }
	            if(term_base.stopword) {
	                double prob_t1 = 0.0;
	                if((t - 1) >= 0 && term_base.G.has_edge(this.terms.get(t-1).id, this.terms.get(t).id)) {
	                    prob_t1 = term_base.G.prob(this.terms.get(t-1).id, this.terms.get(t).id, "TF") / this.terms.get(t-1).tf;
	                }
	                double prob_t2 = 0.0;
	                if(term_base.G.has_edge(this.terms.get(t).id, this.terms.get(t+1).id)) {
	                    prob_t2 = term_base.G.prob(this.terms.get(t).id, this.terms.get(t+1).id, "TF") / this.terms.get(t+1).tf;
	                }
	                double prob = prob_t1 * prob_t2;
	                prod_H *= (1 + (1 - prob ) );
	                sum_H -= (1 - prob);
	            } else {
	                sum_H += term_base.H;
	                prod_H *= term_base.H;
	            }
	        double tf_used = 1.0;
	        if(isNone || isKPF) {
	            tf_used = this.tf;
	        }
	        if(isVirtual) {
	        	tf_used = np.mean(terms.stream().map(term_obj -> term_obj.tf).collect(Collectors.toList()));//tf_used = np.mean( [term_obj.tf for term_obj in self.terms] );
	        }
	        this.H = prod_H / ( ( sum_H + 1 ) * tf_used);
	        }
	    }
	    
	    protected boolean start_or_end_stopwords;
		protected Set<String> tags;
		protected String unique_kw;
		protected List<Term> terms;
		protected int size;
		protected double integrity;
		
		@Override
		protected Double getattr(String feature_name) {
			Double value = super.getattr(feature_name);
			if (value != null) {
				return value;
			}
			if ("integrity".contentEquals(feature_name)) {
				return this.integrity;
			}
			if ("size".contentEquals(feature_name)) { 
				return (double)this.size;
			}
			return null;
		}
	}
	
	public static class DataCore {
		public DataCore(String text, Set<String> stopword_set, int windowsSize, int n, String[] tagsToDiscard, char[] exclude, String language) {//def __init__(self, text, stopword_set, windowsSize, n, tagsToDiscard = set(['u', 'd']), exclude = set(string.punctuation)):
	        this.number_of_sentences = 0;
	        this.number_of_words = 0;
	        this.terms = new HashMap<>();//{}
	        this.candidates = new HashMap<>();//{}
	        this.sentences_obj = new ArrayList<>();// []
	        this.sentences_str = new ArrayList<>();//[]
	        this.G = nx.DiGraph();
	        this.exclude = exclude == null ? PUNCTUATION : exclude;
	        this.tagsToDiscard = tagsToDiscard == null ? new String[] {"u", "d"} : tagsToDiscard;
	        this.freq_ns = new HashMap<>();// {}
	        for (int i = 0; i < n; i++) {
	            this.freq_ns.put(i+1, 0.0);
	        }
	        this.stopword_set = stopword_set;
	        this.language = language;
	        this._build(text, windowsSize, n);
		}
		
		public ComposedWord build_candidate(String candidate_string) {
//	        sentences_str = [w for w in split_contractions(web_tokenizer(candidate_string.toLowerCase())) if not (w.startswith("'") and len(w) > 1) and len(w) > 0]
	        List<String> sentences_str = split_contractions(web_tokenizer(candidate_string.toLowerCase(), this.language), this.language).stream()
	        		.filter(w -> w.length() > 0)
	        		.filter(w -> !(w.startsWith("'") && w.length() > 1))
	        		.collect(Collectors.toList());	        		;
	        List<Tuple<Object>> candidate_terms = new ArrayList<>();
	        for (int i = 0; i < sentences_str.size(); i++) {
	        	String word = sentences_str.get(i);
	            String tag = this.getTag(word, i);
	            Term term_obj = this.getTerm(word, false);//save_non_seen=False)
	            if (term_obj.tf == 0) {
	                term_obj = null;
	            }
	            candidate_terms.add(Tuple.from(tag, word, term_obj));
	        }
	        
	        if (candidate_terms.stream().filter(c -> c.value(2) != null).count() == 0) {//len([cand for cand in candidate_terms if cand[2] != None]) == 0: {
	        	ComposedWord invalid_virtual_cand = new ComposedWord(null);
	        	return invalid_virtual_cand;
	        }
	        ComposedWord virtual_cand = new ComposedWord(candidate_terms);
	        return virtual_cand;
		 }
		
//		Build the datacore features
		public void _build(String text, int windowsSize, int n) {//def _build(self, text, windowsSize, n):
			text = this.pre_filter(text).toString();
//	        this.sentences_str = [ [w for w in split_contractions(web_tokenizer(s)) if not (w.startswith("'") and len(w) > 1) and len(w) > 0] for s in list(split_multi(text)) if len(s.strip()) > 0]
			this.sentences_str = split_multi(text, this.language).stream()
				.filter(s -> s.trim().length() > 0)//strip
				.map(s -> split_contractions(web_tokenizer(s, this.language), this.language).stream()
						.filter(w -> w.length() > 0)
						.filter(w -> !(w.length() > 1 && w.startsWith("'")))
						.collect(Collectors.toList())
						)
				.collect(Collectors.toList());
			
	        this.number_of_sentences = this.sentences_str.size();
	        int pos_text = 0;
	        List<Tuple<Object>> block_of_word_obj = new ArrayList<>();
	        List<Object> sentence_obj_aux = new ArrayList<>();
	        for (int sentence_id = 0; sentence_id < this.sentences_str.size(); sentence_id++) {
	        	List<String> sentence = this.sentences_str.get(sentence_id);
	            sentence_obj_aux = new ArrayList<>();
	            block_of_word_obj = new ArrayList<>();
	            for (int pos_sent = 0; pos_sent < sentence.size(); pos_sent++) {//for (pos_sent, word) in enumerate(sentence):
	            	String word = sentence.get(pos_sent);
	            	int cExclude = 0;//len([c for c in word if c in self.exclude])
	            	for (char c : word.toCharArray()) {
	            		for (char e : this.exclude) {
	            			if (c == e) {
	            				cExclude++;
	            			}
	            		}
	            	}
	                if (cExclude == word.length()) {// len([c for c in word if c in self.exclude]) == len(word): //# If the word is based on exclude chars
	                    if (block_of_word_obj.size() > 0) {
	                        sentence_obj_aux.add( block_of_word_obj );
	                        block_of_word_obj = new ArrayList<>();
	                    }
	                } else {
	                    String tag = this.getTag(word, pos_sent);
	                    Term term_obj = this.getTerm(word);
	                    term_obj.addOccur(tag, sentence_id, pos_sent, pos_text);
	                    pos_text += 1;
//	                    #Create co-occurrence matrix
	                    if (Arrays.stream(this.tagsToDiscard).filter(item -> item.equals(tag)).findFirst().orElse(null) == null) {//  tag not in this.tagsToDiscard:
	                    //  word_windows = list(range( max(0, len(block_of_word_obj)-windowsSize), len(block_of_word_obj) ));
	                        int[] word_windows = IntStream.range(max(0, block_of_word_obj.size()- windowsSize), block_of_word_obj.size()).toArray();
	                        for (int w : word_windows) {
	                        	Tuple<Object> wt = block_of_word_obj.get(w);
	                        	String wTag = (String)wt.value(0);
	                        	if (Arrays.stream(this.tagsToDiscard).filter(item -> item.equals(wTag)).findFirst().orElse(null) == null) {//block_of_word_obj[w][0] not in self.tagsToDiscard
	                        		this.addCooccur((Term)wt.value(2), term_obj);
	                        	}
	                        }
	                    }
//	                    #Generate candidate keyphrase list
	                    List<Tuple<Object>> candidate = new ArrayList<>();
	                    candidate.add(Tuple.from(tag, word, term_obj));
	                    ComposedWord cand = new ComposedWord(candidate);
	                    this.addOrUpdateComposedWord(cand);
	                    // TODO need update
//	                    int[] word_windows = IntStream.range(max(0, block_of_word_obj.size() - (n - 1)), block_of_word_obj.size()).toArray();// list(range( max(0, len(block_of_word_obj)-(n-1)), len(block_of_word_obj) ))[::-1]
	                    for (int w =  block_of_word_obj.size() - 1; w >= max(0, block_of_word_obj.size() - (n - 1)); w--) {	                    	
	                        candidate.add(block_of_word_obj.get(w));
	                        int key = candidate.size();
	                        double old = this.freq_ns.get(key);
	                        this.freq_ns.put(key, old + 1.0);//this.freq_ns[candidate.size()] += 1.0
	                        List<Tuple<Object>> invertCandidate = new ArrayList<>();
	                        for (int i = candidate.size() - 1; i >= 0; i--) {
	                        	invertCandidate.add(candidate.get(i));
	                        }
	                        cand = new ComposedWord(invertCandidate);//composed_word(candidate[::-1])
	                        this.addOrUpdateComposedWord(cand);
	                    }
//	                    # Add term to the block of words' buffer
	                    block_of_word_obj.add(Tuple.from(tag, word, term_obj));
	                }
	            }
	            if (block_of_word_obj.size() > 0) {
	            	sentence_obj_aux.add(block_of_word_obj);
	            }
	            if (sentence_obj_aux.size() > 0) {	            	
	            	this.sentences_obj.add(sentence_obj_aux);
	            }
	        }
	        if (block_of_word_obj.size() > 0) {	        	
	        	sentence_obj_aux.add( block_of_word_obj );
	        }

	        if (sentence_obj_aux.size() > 0) {
	            this.sentences_obj.add(sentence_obj_aux);
	        }
	        this.number_of_words = pos_text;
		}
		
	    public void build_single_terms_features(String[] features) {//def build_single_terms_features(self, features=None):
	    	List<Double> termTFs = new ArrayList<>();
	    	List<Double> termValidTFs = new ArrayList<>();
//	        List<Term> validTerms = 
    		this.terms.values().stream()
		    		.peek(term -> termTFs.add(term.tf))
		    		.filter(term -> !term.stopword)
	        		.peek(term -> termValidTFs.add(term.tf))
	        		.collect(Collectors.toList());// [ term for term in self.terms.values() if not term.stopword ]
	        np validTFs = np.array(termValidTFs);//(np.array([ x.tf for x in validTerms ]))
	        double avgTF = validTFs.mean();
	        double stdTF = validTFs.std();
	        double maxTF = max(termTFs);//max([ x.tf for x in self.terms.values()])
//	        list(map(lambda x: x.updateH(maxTF=maxTF, avgTF=avgTF, stdTF=stdTF, number_of_sentences=self.number_of_sentences, features=features), self.terms.values()))
	        this.terms.values().stream()
	        	.forEach(term -> term.updateH(maxTF, avgTF, stdTF, this.number_of_sentences, features));
		}
		
	    public void build_single_terms_features() {
	    	this.build_single_terms_features(null);
	    }
	    
	    public void build_mult_terms_features(String[] features) {//def build_mult_terms_features(self, features=None):
//	        list(map(lambda x: x.updateH(features=features), [cand for cand in self.candidates.values() if cand.isValid()]))
	        this.candidates.values().stream()
	        	.filter(ComposedWord::isValid)
	        	.forEach(cand -> cand.updateH(features));
	    }
	    
	    public void build_mult_terms_features() {
	    	this.build_mult_terms_features(null);
	    }
		
	    public String pre_filter(String text) {
	        Pattern prog = Pattern.compile("^(\\s*([A-Z]))");
	        String[] parts = text.split("\n");
	        StringBuffer buffer = new StringBuffer();
	        for(String part : parts) {
	            String sep = " ";
	            if(prog.matcher(part).find()) {
	                sep = "\n\n";
	            }
	            buffer.append(sep).append(part.replace('\t',' '));
	        }
	        return buffer.toString();
	    }
		
	    public String getTag(String word, int i) {
	        try {
	            String w2 = word.replace(",","");
	            Float.parseFloat(w2);// float(w2)
	            return "d";
	        } catch (Exception ex) {
	            int cdigit = 0;//len([c for c in word if c.isdigit()])
	            int calpha = 0;//len([c for c in word if c.isalpha()])
	            int cexclude = 0;//len([c for c in word if c in this.exclude]
	            int csuper = 0;//len([c for c in word if c.isupper()
	            char[] words = word.toCharArray();
	            for (char c : words) {
	            	if (Character.isDigit(c)) {//isdigit
	            		cdigit++;
	            	} 
	            	if (Character.isAlphabetic(c)) {//isalpha
	            		calpha++;
	            	}
	            	if (Character.isUpperCase(c)) {//issuper
	            		csuper++;
	            	}
	            	for (char _c : this.exclude) {//c in this.exclude
	            		if (c == _c) {
	            			cexclude++;
	            		}
	            	}
	            }
	            if ((cdigit > 0 && calpha > 0) || (cdigit == 0 && calpha == 0) || cexclude > 1) {
	                return "u";
	            }
	            if(word.length() == csuper) {
	                return "a";
	            }
	            if (csuper == 1 && word.length() > 1 && Character.isUpperCase(words[0]) && i > 0) {
	                return "n";
	            }
	        }
	        return "p";
	    }
		
	    public Term getTerm(String str_word, boolean save_non_seen) {//getTerm(self, str_word, save_non_seen=True):
	        String unique_term = str_word.toLowerCase();
	        boolean simples_sto = this.stopword_set.contains(unique_term); //unique_term in this.stopword_set
	        if(unique_term.endsWith("s") && unique_term.length() > 3) {
	        	unique_term = unique_term.substring(0, unique_term.length() - 1);//unique_term = unique_term[:-1]
	        }
	        if(this.terms.containsKey(unique_term)) {
	            return this.terms.get(unique_term);
	        }
//	        # Include this part
	        String simples_unique_term = unique_term;
	        for(char pontuation : this.exclude) {
	            simples_unique_term = simples_unique_term.replace(pontuation + "", "");//''
	        }
//	        # until here
	        boolean isstopword = simples_sto || this.stopword_set.contains(unique_term) || simples_unique_term.length() < 3;
	        
	        int term_id = this.terms.size();
	        Term term_obj = new SingleWord(unique_term, term_id, this.G);
	        term_obj.stopword = isstopword;

	        if(save_non_seen) {
	            this.G.add_node(term_id);
	            this.terms.put(unique_term, term_obj);
	        }
	        return term_obj;
	    }
	    public Term getTerm(String str_word) {
	    	return this.getTerm(str_word, true);
	    }
		public void addCooccur(Term left_term, Term right_term) {
	        if(!this.G.check_contain(left_term.id, right_term.id)) {// right_term.id not in self.G[left_term.id]:
//	        	self.G.add_edge(left_term.id, right_term.id, TF=0.)
	        	Map<String, Object> d = new HashMap<String, Object>();
	        	d.put("TF", 0.0);
	            this.G.add_edge(left_term.id, right_term.id, d);
	        }
	        //self.G[left_term.id][right_term.id]["TF"]+=1.
	        double prob = this.G.prob(left_term.id, right_term.id, "TF");//+=1.0;
	        this.G.prob(left_term.id, right_term.id, "TF", prob + 1.0);
		}
	    public void addOrUpdateComposedWord(ComposedWord cand) {
	        if(!this.candidates.containsKey(cand.unique_kw)) {
	            this.candidates.put(cand.unique_kw, cand);
	        } else {
	            this.candidates.get(cand.unique_kw).uptadeCand(cand);
	        }
	        this.candidates.get(cand.unique_kw).tf += 1.0;
	    }
		
	    protected int number_of_sentences;
	    protected int number_of_words;
	    protected Map<String, Term> terms;
	    protected Map<String, ComposedWord> candidates;//???
	    protected List<Object> sentences_obj;
        protected List<List<String>> sentences_str;
        protected nx G;
        protected char[] exclude;
        protected String[] tagsToDiscard;
        protected Map<Integer, Double> freq_ns;
        protected Set<String> stopword_set;
        protected String language;
	}
}
