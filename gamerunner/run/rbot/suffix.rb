class Suffix
	attr_reader :suffix,:string

def initialize(string)
	@string=string
	@suffix=(0..string.size-1).sort_by{|i|@string[i..-1]}
end

def print_suffix

	print @suffix

end

end
