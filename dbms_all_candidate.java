import java.util.*;

class FD
{
	Set<String> key;
	Set<String> specifies;
	
	public FD() {
		key = new TreeSet<>();
		specifies = new TreeSet<>();
	}
	
	public void addKey(String str)
	{
		key.add(str);
	}
	
	public void addSpecifies(String str)
	{
		specifies.add(str);
	}
	
	public void printKeys()
	{
		System.out.println(key);
	}
	
	public void printSpecifies()
	{
		System.out.println(specifies);
	}
	
}

public class dbms_all_candidate {
	static void combinationUtil(String arr[], int n, int r, int index, String data[], int i, ArrayList<Set<String>> subsets) 
	{ 
		if (index == r) 
		{
			Set<String> s_set = new TreeSet<>();
			for (int j=0; j<r; j++) 
			{ 
				//System.out.print(data[j]+" ");
				s_set.add(data[j]);
			}
			subsets.add(s_set);
		//System.out.println(""); 
		return; 
		} 
 
	if (i >= n) 
	return; 

	data[index] = arr[i]; 
	combinationUtil(arr, n, r, index+1, data, i+1, subsets); 
 
	combinationUtil(arr, n, r, index, data, i+1, subsets); 
	} 
	
	static void makeClosure(int key_i, Set<String> closure_set, FD[] fd, int numFD, boolean visited[])
	{
		visited[key_i] = Boolean.TRUE;
		String[] arr = fd[key_i].specifies.toArray(new String[0]);
	    int n = arr.length;
	    ArrayList<Set<String>> subsets = new ArrayList<Set<String>>();
		
	    for(int r = 1; r <= n; r++)
	    {
	    	String data[]=new String[r];
	    	combinationUtil(arr, n, r, 0, data, 0, subsets);
	    }
	    int tot_sets = subsets.size();
	    for(int l = 0; l < tot_sets; l++)
	    {
	    	Set<String> temp_set = subsets.get(l);
	    	//System.out.println(fd[key_i].key + " gives " + temp_set);
	    	if(temp_set.size() == 1)
	    	{
	    		String[] temp_elem = temp_set.toArray(new String[0]);
	    		closure_set.add(temp_elem[0]);
	    	}
	    	
	    	for(int m = 0; m < numFD; m++)
	    	{
	    		
	    		if(temp_set.equals(fd[m].key) && !temp_set.equals(fd[key_i].key) && !visited[m])
	    		{
	    			makeClosure(m, closure_set, fd, numFD, visited);
	    		}
	    	}
	    }
	    
	}
	
	static void getSecond(FD fd, ArrayList<Set<String>> part_candidate, ArrayList<FD> fd_second, Set<String> non_key_attributes)
	{
		int sec_flag = 0;
		Set<String> temp_specifies = new TreeSet<>();
//		for(Set<String> temp_set: part_candidate)
//		{
//			if(temp_set.equals(fd.key))
//				flag = 1;
//		}
		if(part_candidate.contains(fd.key))
		{
			for(String item: fd.specifies)
			{
				if(non_key_attributes.contains(item))
				{
					sec_flag = 1;
					temp_specifies.add(item);
				}
			}
		}
		if(sec_flag == 1)
		{
			FD temp_fd = new FD();
			temp_fd.key = fd.key;
			temp_fd.specifies = temp_specifies;
			fd_second.add(temp_fd);
		}
	}
	
	static void getThird(FD fd, ArrayList<Set<String>> candidate_keys, ArrayList<FD> fd_third, Set<String> non_key_attributes)
	{
		FD temp_orig = new FD();
		temp_orig.key.addAll(fd.key);
		temp_orig.specifies.addAll(fd.specifies);
		temp_orig.specifies.removeAll(fd.key);
		
		int third_flag = 0, flag = 0;
		for(Set<String> item: candidate_keys)
		{
			if(temp_orig.key.containsAll(item))
			{
				third_flag = 1;
				
			}
		}
		Set<String> temp_specifies = new TreeSet<>();
		
		if(third_flag == 0)
		{
			
			for(String str: temp_orig.specifies)
			{
				if(non_key_attributes.contains(str))
				{
					flag = 1;
					temp_specifies.add(str);
				}
			}
		}
		
		if(flag == 1)
		{
			FD temp_fd = new FD();
			temp_fd.key.addAll(temp_orig.key);
			temp_fd.specifies.addAll(temp_specifies);
			fd_third.add(temp_fd);
		}
		
	}
	
	static void getBCNF(FD fd, ArrayList<Set<String>> candidate_keys, ArrayList<FD> fd_bcnf, Set<String> key_attributes)
	{
		FD temp_orig = new FD();
		temp_orig.key.addAll(fd.key);
		temp_orig.specifies.addAll(fd.specifies);
		temp_orig.specifies.removeAll(fd.key);
		
		int bcnf_flag = 0, flag = 0;
		for(Set<String> item: candidate_keys)
		{
			if(temp_orig.key.containsAll(item))
			{
				bcnf_flag = 1;
				
			}
		}
		Set<String> temp_specifies = new TreeSet<>();
		
		if(bcnf_flag == 0)
		{
			
			for(String str: temp_orig.specifies)
			{
				if(key_attributes.contains(str))
				{
					flag = 1;
					temp_specifies.add(str);
				}
			}
		}
		
		if(flag == 1)
		{
			FD temp_fd = new FD();
			temp_fd.key.addAll(temp_orig.key);
			temp_fd.specifies.addAll(temp_specifies);
			fd_bcnf.add(temp_fd);
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int normal_form = -1;
		
		// take attributes
		
		System.out.println("Please enter the Attributes: ");
		
		Scanner sc = new Scanner(System.in);
		String attributes = sc.nextLine();
		
		StringTokenizer st = new StringTokenizer(attributes, ",");
		int numAtt = st.countTokens();
		
		Set<String> attribute_set = new TreeSet<>();
		Set<String> dupli = new TreeSet<>();

		while(st.hasMoreTokens())
		{
			String temp = st.nextToken();
			if(attribute_set.contains(temp))
			{
				normal_form = 0;
				dupli.add(temp);
			}
				
		attribute_set.add(temp);
		}
		
		if(normal_form == 0)
		{
			System.out.println("Highest Normal Form: ");
			System.out.println("It doesn't even satisfies 1-NF");
			System.out.println("");	
			System.out.println("Decomposition for 1-NF :");
			System.out.println("Use different attribute names for " + dupli);
			sc.close();
			return;
		}
		
		// take functional dependencies
		
		int fd_i = 0;
		System.out.println("Please enter the Functional Dependencies: ");
		String fd_list = sc.nextLine();
		st = new StringTokenizer(fd_list, ";");
		int numFD = st.countTokens();
		FD[] fd = new FD[100];
		
		while(st.hasMoreTokens())
		{
			String str = st.nextToken();
			fd[fd_i] = new FD();
			int key_flag = 1, start = 0;
			for(int ind = 0; ind < str.length(); ind++) 
			{
				if(str.charAt(ind) == '>')
				{
					key_flag = 0;
					String substr = str.substring(start, ind);
					fd[fd_i].addKey(substr);
					fd[fd_i].addSpecifies(substr);
					start = ind+1;
				}
					
				else if(str.charAt(ind)==',' && key_flag == 1)
				{
					String substr = str.substring(start, ind);
					fd[fd_i].addKey(substr);
					fd[fd_i].addSpecifies(substr);
					start = ind+1;
				}
				else if(str.charAt(ind)==',' && key_flag == 0)
				{
					String substr = str.substring(start, ind);
					fd[fd_i].addSpecifies(substr);
					start = ind+1;
				}
			}
			String substr = str.substring(start, str.length());
			fd[fd_i].addSpecifies(substr);
			//attribute_set.addAll(fd[fd_i].specifies);
			fd_i++;
			
		}
		//int numAtt = attribute_set.size();
		
		// make original FDs
		FD[] fd_orig = new FD[numFD];
		int numOrigFD = numFD;
		for(int i = 0; i < numFD; i++)
		{
			fd_orig[i] = new FD();
			fd_orig[i].key.addAll(fd[i].key);
			fd_orig[i].specifies.addAll(fd[i].specifies);
		}
		
		// print all original fds
		System.out.println("");
		System.out.println("The Functional Dependencies are:");
		for(int key_i = 0; key_i < numOrigFD; key_i++)
		{
			Set<String> temp_set = new TreeSet<>();
			temp_set.addAll(fd_orig[key_i].specifies);
			temp_set.removeAll(fd_orig[key_i].key);
			System.out.println(fd_orig[key_i].key + " -> " + temp_set);
		}
		
		Set<String> key_attributes = new TreeSet<>();
		Set<String> non_key_attributes = new TreeSet<>();
		
		ArrayList<Set<String>> candidate_keys = new ArrayList<Set<String>>();
		ArrayList<Set<String>> temp_candidate_keys = new ArrayList<Set<String>>();
		ArrayList<Set<String>> red_candidate_keys = new ArrayList<Set<String>>();
		
		// make closure of sets
		
		int closure_flag = 1;
		while(closure_flag == 1)
		{
			closure_flag = 0;
		for(int key_i = 0; key_i < numFD; key_i++)
		{
			Set<String> closure_set = new TreeSet<>();
			boolean visited[] = new boolean[numFD];
			Arrays.fill(visited, Boolean.FALSE);
			makeClosure(key_i, closure_set, fd, numFD, visited);
			if(!closure_set.equals(fd[key_i].specifies))
				closure_flag = 1;
			fd[key_i].specifies.addAll(closure_set);
			if(fd[key_i].specifies.size() == numAtt)
			{
				if(!candidate_keys.contains(fd[key_i].key))
				{
					candidate_keys.add(fd[key_i].key);
				}
			}
//			for(String item: closure_set)
//			{
//				fd[key_i].specifies.add(item);
//			}
		}
		}
		temp_candidate_keys.addAll(candidate_keys);
		
		// remove redundant keys
		for(Set<String> item1: candidate_keys)
		{
			for(Set<String> item2: candidate_keys)
			{
				if(!item1.equals(item2) && item1.containsAll(item2))
				{
					red_candidate_keys.add(item1);
				}
			}
		}
		candidate_keys.removeAll(red_candidate_keys);
		
		
		
		
		
		// printing candidate keys
		
		
//		for(int key_i = 0; key_i < numFD; key_i++)
//		{
//			String[] arr = fd[key_i].key.toArray(new String[0]);
//		    int n = arr.length;
//		    ArrayList<Set<String>> subsets = new ArrayList<Set<String>>();
//			
//		    for(int r = 1; r < n; r++)
//		    {
//		    	String data[]=new String[r];
//		    	combinationUtil(arr, n, r, 0, data, 0, subsets);
//		    }
//		    
//		    int flag = 1;
//		    int tot_sets = subsets.size();
//		    for(int l = 0; l < tot_sets; l++)
//		    {
//		    	Set<String> temp_set = subsets.get(l);
//		    	for(int m = 0; m < numFD; m++)
//		    	{
//		    		if(temp_set.equals(fd[m].key) && fd[m].specifies.size() == numAtt)
//		    		{
//		    			flag = 0;
//		    			break;
//		    		}
//		    	}
//		    	if(flag == 0)
//		    		break;
//		    }
//		    
//			if(flag == 1 && fd[key_i].specifies.size() == numAtt)
//			{
//				candidate_keys.add(fd[key_i].key);
//				System.out.println(fd[key_i].key);
//				for(String item: fd[key_i].key)
//				{
//					key_attributes.add(item);
//				}
//			}
//				
//		}
		
		// get all candidate key
		double num_fds = Math.pow(2, attribute_set.size()) - 1;
		int num_fd = (int)num_fds;
		FD[] fd_all = new FD[num_fd];
		{
			String[] arr = attribute_set.toArray(new String[0]);
		    int n = arr.length;
		    ArrayList<Set<String>> subsets = new ArrayList<Set<String>>();
			
		    for(int r = 1; r <= n; r++)
		    {
		    	String data[]=new String[r];
		    	combinationUtil(arr, n, r, 0, data, 0, subsets);
		    }
		    
		    int tot_sets = subsets.size();
		    for(int l = 0; l < tot_sets; l++)
		    {
		    	Set<String> temp_set = subsets.get(l);
		    	fd_all[l] = new FD();
		    	fd_all[l].key = temp_set;
		    	
		    	for(int m = 0; m < numFD; m++)
		    	{
		    		if(temp_set.equals(fd[m].key))
		    		{
		    			fd_all[l].specifies = fd[m].specifies;
		    			break;
		    		}
		    	}
		    	fd_all[l].specifies.addAll(temp_set);
//		    	for(String item: temp_set)
//		    	{
//		    		fd_all[l].specifies.add(item);
//		    	}
		    }
		    closure_flag = 1;
			while(closure_flag == 1)
			{
				closure_flag = 0;
			for(int key_i = 0; key_i < tot_sets; key_i++)
			{
				int flag = 1;
				for(Set<String> item: temp_candidate_keys)
				{
					if(fd_all[key_i].key.containsAll(item))
					{
						flag = 0;
						break;
					}
				}
				if(flag == 0)
					continue;
				Set<String> closure_set = new TreeSet<>();
				boolean visited[] = new boolean[tot_sets];
				Arrays.fill(visited, Boolean.FALSE);
				makeClosure(key_i, closure_set, fd_all, tot_sets, visited);
				if(!closure_set.equals(fd_all[key_i].specifies))
					closure_flag = 1;
				fd_all[key_i].specifies.addAll(closure_set);
				if(fd_all[key_i].specifies.size() == numAtt)
				{
					if(!temp_candidate_keys.contains(fd_all[key_i].key))
					{
						temp_candidate_keys.add(fd_all[key_i].key);
					}
				}
				
//				for(String item: closure_set)
//				{
//					fd_all[key_i].specifies.add(item);
//				}
//				if(fd_all[key_i].specifies.size() == numAtt)
//				{
//					closure_flag = 0;
//					break;
//				}
			}
			}
		    
			for(int key_i = 0; key_i < tot_sets; key_i++)
			{
				int flag = 1;
//				ArrayList<Set<String>> part_set = new ArrayList<Set<String>>();
//				String[] arr1 = fd_all[key_i].key.toArray(new String[0]);
//			    int n1 = arr1.length;
//			   
//			    for(int r = 1; r < n; r++)
//			    {
//			    	String data[]=new String[r];
//			    	combinationUtil(arr1, n1, r, 0, data, 0, part_set);
//			    }
//			    for(Set<String> item: part_set)
//			    {
//			    	if(candidate_keys.contains(item))
//			    	{
//			    		flag = 0;
//			    		break;
//			    	}
//			    }  
			    
				for(Set<String> item: candidate_keys)
				{
					if(fd_all[key_i].key.containsAll(item))
					{
						flag = 0;
						break;
					}
				}
				
				if(fd_all[key_i].specifies.size() == numAtt && flag == 1)
				{
					if(!candidate_keys.contains(fd_all[key_i].key))
					{
						candidate_keys.add(fd_all[key_i].key);
						fd[numFD] = new FD();
						fd[numFD].key.addAll(fd_all[key_i].key);
						fd[numFD].specifies.addAll(fd_all[key_i].specifies);
						numFD++;
					}
//					for(String item: fd_all[key_i].key)
//					{
//						key_attributes.add(item);
//					}
					
					//break;
				}
			}
		}
		
		//System.out.println("numFD = " + numFD);
		
		// print all FDs
//				System.out.println("All the Functional dependencies are: ");
//				for(int k = 0; k < numFD; k++)
//				{
//					System.out.println(fd[k].key + " -> " + fd[k].specifies);
//				}
		
		// remove redundant keys
				for(Set<String> item1: candidate_keys)
				{
					for(Set<String> item2: candidate_keys)
					{
						if(!item1.equals(item2) && item1.containsAll(item2))
						{
							red_candidate_keys.add(item1);
						}
					}
				}
				candidate_keys.removeAll(red_candidate_keys);
				
						
		// printing candidate keys		
		
//		System.out.println("The candidate keys are:");
//		for(Set<String> item: candidate_keys)
//		{
//			System.out.println(item);
//		}
		
		
		
		for(Set<String> item: candidate_keys)
		{
			key_attributes.addAll(item);
		}
		
		for(String item: attribute_set)
		{
			if(!key_attributes.contains(item))
				non_key_attributes.add(item);
		}
		
		// print key and non_key
		
//		System.out.println("Key attributes are: " + key_attributes);
//		System.out.println("Non-Key attributes are: " + non_key_attributes);
		
		// print all fds
//		for(int key_i = 0; key_i < num_fd; key_i++)
//		{
//			System.out.println(fd_all[key_i].key + " -> " + fd_all[key_i].specifies);
//		}
		
		// get all part of candidate keys
		ArrayList<Set<String>> part_candidate = new ArrayList<Set<String>>();
		for(int i = 0; i < candidate_keys.size(); i++)
		{
			String[] arr = candidate_keys.get(i).toArray(new String[0]);
		    int n = arr.length;
		   
		    for(int r = 1; r < n; r++)
		    {
		    	String data[]=new String[r];
		    	combinationUtil(arr, n, r, 0, data, 0, part_candidate);
		    }
		}
//		System.out.println("Part of candidate keys are: " + part_candidate);
		
		// All related to 2nd normal form
		
		//boolean second[] = new boolean[numFD];
		//Arrays.fill(second, false);
		ArrayList<FD> fd_second = new ArrayList<FD>();
		
		for(int i = 0; i < numOrigFD; i++)
		{
			getSecond(fd_orig[i], part_candidate, fd_second, non_key_attributes);
		}
		
		if(!fd_second.isEmpty())
		{
			System.out.println("");
			System.out.println("The given Relation is in 1-NF");
			System.out.println("Attributes : " + attribute_set);
			System.out.print("Candidate key(s) : ");
			for(Set<String> item: candidate_keys)
			{
				System.out.print(item + " ");
			}
			System.out.println("");
			System.out.println("");
			System.out.println("FDs violating 2-NF are : ");
			for(FD temp_fd: fd_second)
			{
				System.out.println(temp_fd.key + " -> " + temp_fd.specifies);
				
			}
			
			
			// try this
			for(FD temp_fd: fd_second)
			{
				// dare
				temp_fd.specifies.addAll(temp_fd.key);
				ArrayList<Set<String>> part_specifies = new ArrayList<Set<String>>();
					String[] arr = temp_fd.specifies.toArray(new String[0]);
				    int n = arr.length;
				   
				    for(int r = 1; r <= n; r++)
				    {
				    	String data[]=new String[r];
				    	combinationUtil(arr, n, r, 0, data, 0, part_specifies);
				    }
				Set<String> new_specifies = new TreeSet<>();
				for(int i = 0; i < part_specifies.size(); i++)
				{
					if(candidate_keys.contains(part_specifies.get(i)))
						continue;
					if(key_attributes.containsAll(part_specifies.get(i)))
						continue;
					//Set<String> temp_set = new TreeSet<>();
					for(int j = 0; j < numOrigFD; j++)
					{
						if(fd_orig[j].key.equals(part_specifies.get(i)))
						{
							for(String str: fd_orig[j].specifies)
							{
								if(!key_attributes.contains(str))
									new_specifies.add(str);
							}
						}
					}
				}
				temp_fd.specifies.addAll(new_specifies);
			}
			
			// end try
			
			// Printing 2nd normal forms
			Set<String> base_attributes = new TreeSet<>();
			Set<String> base_key = new TreeSet<>();
			base_attributes.addAll(attribute_set);
			for(FD temp_fd: fd_second)
			{
				//attribute_set.removeAll(temp_fd.specifies);
				for(String str: temp_fd.specifies)
				{
					//dare
					if(base_attributes.contains(str) && !key_attributes.contains(str))
					{
						base_attributes.remove(str);
					}
				}
			}
			
			//make base_key apt
			
			int got_key = 0, i_size = 1;
			while(got_key == 0 && i_size <= base_attributes.size())
			{
				for(int i = 0; i < numFD; i++)
				{
					if(fd[i].key.size() == i_size)
					{
						int flag = 1;
						for(String str: base_attributes)
						{
							if(!fd[i].specifies.contains(str))
							{
								flag = 0;
								break;
							}
						}
						if(flag == 1)
						{
							got_key = 1;
							base_key.addAll(fd[i].key);
							break;	
						}
						
					}
				}
				i_size++;
			}
			
			// remove 1NF in base relation - JUGAAD
			{
				ArrayList<Set<String>> part_base_key = new ArrayList<Set<String>>();
					String[] arr = base_key.toArray(new String[0]);
				    int n = arr.length;
				   
				    for(int r = 1; r < n; r++)
				    {
				    	String data[]=new String[r];
				    	combinationUtil(arr, n, r, 0, data, 0, part_base_key);
				    }
				Set<String> partial = new TreeSet<>();
				for(int i = 0; i < part_base_key.size(); i++)
				{
					
					for(int j = 0; j < numOrigFD; j++)
					{
						if(fd_orig[j].key.equals(part_base_key.get(i)))
						{
							for(String str: fd_orig[j].specifies)
							{
								if(!base_key.contains(str) && base_attributes.contains(str))
								{
									partial.add(str);
								}
							}
						}
					}
				}
				base_attributes.removeAll(partial);
			}
			
			//add un-specified in base key 
			Set<String> add_it = new TreeSet<>();
			for(int i = 0; i < numFD; i++)
			{
				if(fd[i].key.equals(base_key))
				{
					for(String str: base_attributes)
					{
						if(!fd[i].specifies.contains(str))
							add_it.add(str);
					}
				}
			}
			base_key.addAll(add_it);
			System.out.println("");
			System.out.println("The decomposed Relations for higher NF are: ");
			System.out.println("");
			System.out.println("For Relation-1");
			System.out.println("Attributes: " + base_attributes);
			System.out.println("Candidate key: " + base_key);
			
			int rel_ind = 2;
			for(FD temp_fd: fd_second)
			{
				Set<String> temp_set = new TreeSet<>();
				temp_set.addAll(temp_fd.key);
				temp_set.addAll(temp_fd.specifies);
				System.out.println("");
				System.out.println("For Relation-" + rel_ind++);
				System.out.println("Attributes: " + temp_set);
				System.out.println("Candidate key: " + temp_fd.key);
			}
			
			sc.close();
			return;
		}
		
		// All about 3rd NF
		ArrayList<FD> fd_third = new ArrayList<FD>();
		
		for(int i = 0; i < numOrigFD; i++)
		{
			getThird(fd_orig[i], candidate_keys, fd_third, non_key_attributes);
		}
		
		if(!fd_third.isEmpty())
		{
			System.out.println("");
			System.out.println("The given Relation is in 2-NF");
			System.out.println("Attributes : " + attribute_set);
			System.out.print("Candidate key(s) : ");
			for(Set<String> item: candidate_keys)
			{
				System.out.print(item + " ");
			}
			System.out.println("");
			System.out.println("");
			System.out.println("FDs violating 3-NF are : ");
			for(FD temp_fd: fd_third)
			{
				System.out.println(temp_fd.key + " -> " + temp_fd.specifies);
				
			}
			
			// Printing 3rd normal forms
						Set<String> base_attributes = new TreeSet<>();
						ArrayList<Set<String>> base_keys = new ArrayList<>();
						base_attributes.addAll(attribute_set);
						for(FD temp_fd: fd_third)
						{
							//attribute_set.removeAll(temp_fd.specifies);
							for(String str: temp_fd.specifies)
							{
								//dare
								if(base_attributes.contains(str) && !key_attributes.contains(str))
								{
									base_attributes.remove(str);
								}
							}
						}
						
						//make base_key apt
						
//						int got_key = 0, i_size = 1;
//						while(got_key == 0 && i_size <= base_attributes.size())
//						{
//							for(int i = 0; i < numFD; i++)
//							{
//								if(fd[i].key.size() == i_size)
//								{
//									int flag = 1;
//									for(String str: base_attributes)
//									{
//										if(!fd[i].specifies.contains(str))
//										{
//											flag = 0;
//											break;
//										}
//									}
//									if(flag == 1)
//									{
//										got_key = 1;
//										base_key.addAll(fd[i].key);
//										break;	
//									}
//									
//								}
//							}
//							i_size++;
//						}
						
						for(Set<String> temp_cand: candidate_keys)
						{
							if(base_attributes.containsAll(temp_cand))
								base_keys.add(temp_cand);
						}
						System.out.println("");
						System.out.println("The decomposed Relations for higher NF are: ");
						System.out.println("");
						System.out.println("For Relation-1");
						System.out.println("Attributes: " + base_attributes);
						System.out.println("Candidate key(s): " + base_keys);
						
						int rel_ind = 2;
						for(FD temp_fd: fd_third)
						{
							Set<String> temp_set = new TreeSet<>();
							temp_set.addAll(temp_fd.key);
							temp_set.addAll(temp_fd.specifies);
							System.out.println("");
							System.out.println("For Relation-" + rel_ind++);
							System.out.println("Attributes: " + temp_set);
							System.out.println("Candidate key: " + temp_fd.key);
						}
						
						sc.close();
						return;		
				
		}
		
		// All related to BCNF

			ArrayList<FD> fd_bcnf = new ArrayList<FD>();
			
			for(int i = 0; i < numOrigFD; i++)
			{
				getBCNF(fd_orig[i], candidate_keys, fd_bcnf, key_attributes);
			}
			
			if(!fd_bcnf.isEmpty())
			{
			
				System.out.println("");
				System.out.println("The given Relation is in 3-NF");
				System.out.println("Attributes : " + attribute_set);
				System.out.print("Candidate key(s) : ");
				for(Set<String> item: candidate_keys)
				{
					System.out.print(item + " ");
				}
				System.out.println("");
				System.out.println("");
				System.out.println("FDs violating BCNF are : ");
				for(FD temp_fd: fd_bcnf)
				{
					System.out.println(temp_fd.key + " -> " + temp_fd.specifies);
					
				}
			
			// Printing BCNF normal forms
			Set<String> base_attributes = new TreeSet<>();
			ArrayList<Set<String>> base_keys = new ArrayList<>();
			base_attributes.addAll(attribute_set);
			
			for(FD temp_fd: fd_bcnf)
			{
				//attribute_set.removeAll(temp_fd.specifies);
				for(String str: temp_fd.key)
				{
					//dare
					if(base_attributes.contains(str) && !candidate_keys.get(0).contains(str))
					{
						base_attributes.remove(str);
					}
				}
			}
			
			//make base_key apt
			
//			int got_key = 0, i_size = 1;
//			while(got_key == 0 && i_size <= base_attributes.size())
//			{
//				for(int i = 0; i < numFD; i++)
//				{
//					if(fd[i].key.size() == i_size)
//					{
//						int flag = 1;
//						for(String str: base_attributes)
//						{
//							if(!fd[i].specifies.contains(str))
//							{
//								flag = 0;
//								break;
//							}
//						}
//						if(flag == 1)
//						{
//							got_key = 1;
//							base_key.addAll(fd[i].key);
//							break;	
//						}
//						
//					}
//				}
//				i_size++;
//			}
//			
//			for(FD temp_fd: fd_bcnf)
//			{
//				//attribute_set.removeAll(temp_fd.specifies);
//				for(String str: temp_fd.key)
//				{
//					//dare
//					if(base_attributes.contains(str) && !base_key.contains(str))
//					{
//						base_attributes.remove(str);
//					}
//				}
//			}
			
			for(Set<String> temp_cand: candidate_keys)
			{
				if(base_attributes.containsAll(temp_cand))
					base_keys.add(temp_cand);
			}
			
			int rel_ind = 1;
			System.out.println("");
			System.out.println("The decomposed Relations for higher NF are: ");
			if(!base_attributes.isEmpty())
			{
				rel_ind = 2;
			System.out.println("");
			System.out.println("For Relation-1");
			System.out.println("Attributes: " + base_attributes);
			System.out.println("Candidate key(s): " + base_keys);
			}
			
			for(FD temp_fd: fd_bcnf)
			{
				Set<String> temp_set = new TreeSet<>();
				temp_set.addAll(temp_fd.key);
				temp_set.addAll(temp_fd.specifies);
				System.out.println("");
				System.out.println("For Relation-" + rel_ind++);
				System.out.println("Attributes: " + temp_set);
				System.out.println("Candidate key: " + temp_fd.key);
			}
			
			sc.close();	
			return;
		}
			
			//In BCNF form
			System.out.println("");
			System.out.println("The given Relation is in BCNF");
			System.out.println("Attributes : " + attribute_set);
			System.out.print("Candidate key(s) : ");
			for(Set<String> item: candidate_keys)
			{
				System.out.print(item + " ");
			}
		sc.close();
	}

}
//A,B,C,D,E,F,G,H,I,J
//A>D;B>A;B,C>D;A,C>B,E - 1NF
//B,C>D;A,C>B,E;B>E - 2NF
//B>A;A>C;B,C>D;A,C>B,E - BCNF
//A>B;B>D;C>E - 1NF
//A,B,C>D,E,F;A,B>E,F;A>B
//A,B>C;A,D>G,H;B,D>E,F;A>I;H>J - 1NF
//A>B;B>E;C>D - 1NF
//A,B>C;D>E - 1NF
//A,B>C;A>D,E;B>F;F>G,H - 1NF
//A>B;B,C>E;D,E>A - 3NF